import sys
import zipfile
import subprocess

# html_head = """
#     <head><meta http-equiv="Content-Type" content="text/html; charset=UTF-8"><link href="css/style.css" rel="stylesheet"><script defer="" src="js/script.js"></script><style>
# * {
#   font-family:
#     -apple-system,
#     "system-ui",
#     BlinkMacSystemFont,
#     "Segoe UI",
#     Helvetica,
#     Arial,
#     sans-serif,
#     "Apple Color Emoji",
#     "Segoe UI Emoji";
# }
#
# body {
#   padding: 15px;
#   font-size: 14px;
#   line-height: 1.5;
#   word-wrap: break-word;
# }
#
# h1, h2, h3, h4, h5 {
#   margin-top: 24px;
#   margin-bottom: 16px;
#   font-weight: 600;
# }
#
# h1 {
#   margin-top: 0px;
#   border-bottom: 1px solid rgb(216, 222, 228);
#   font-size: 2em;
#   padding-bottom: 0.3em;
# }
#
# h3 {
#   font-size: 1.25em;
# }
#
# h4 {
#   font-size: 1em;
# }
#
# h3 code, h4 code {
#   font-size: 100%;
# }
#
# a {
#   color: rgb(9, 105, 218);
#   text-decoration: none;
# }
#
# a:hover {
#   text-decoration: underline;
# }
#
# b {
#   font-weight: 600;
# }
#
# code, pre {
#   font-family:
#     ui-monospace,
#     SFMono-Regular,
#     SF Mono,
#     Menlo,
#     Consolas,
#     Liberation Mono,
#     monospace;
#   font-size: 85%;
#   border-radius: 6px;
# }
#
# code {
#   background-color: rgba(175, 184, 193, 0.2);
#   padding: 2.38px 4.76px;
# }
#
# pre {
#   background-color: rgb(246, 248, 250);
#   padding: 16px;
# }
#
# table, td, th {
#   border-color: rgb(208, 215, 222);
#   border-width: 1px;
#   border-style: solid;
#   border-collapse: collapse;
# }
#
# td, th {
#   padding: 6px 13px;
# }
#
# tr:nth-child(even) {
#   background-color: rgb(234, 238, 242);
# }
#
# .white_table tr:nth-child(even) {
#   background-color: rgb(255, 255, 255);
# }
#
# sup>a::before {
#   content: "[";
# }
#
# sup>a::after {
#   content: "]";
# }
#
# .diff-plus, .diff-minus {
#   font-family:
#     ui-monospace,
#     SFMono-Regular,
#     SF Mono,
#     Menlo,
#     Consolas,
#     Liberation Mono,
#     monospace;
# }
#
# .diff-plus {
#   color: rgb(17, 99, 41);
#   background-color: rgb(218, 251, 225);
# }
#
# .diff-minus {
#   color: rgb(130, 7, 30);
#   background-color: rgb(255, 235, 233);
# }
#
# img {
#   width: 350px;
# }
#
# .perfetto {
#   color: rgb(9, 105, 218);
# }
#
# .perfetto:hover {
#   cursor: pointer;
#   text-decoration: underline;
# }
# </style></head>
#     """

kb_in_bytes = 1024
mb_in_bytes = 1024 * 1024

# command executor
def execute_command(command):
    # Run the command using subprocess
    process = subprocess.Popen(command, shell=True, stdout=subprocess.PIPE, stderr=subprocess.PIPE)
    output, error = process.communicate()

    return output.decode()

# add/update the value (i.e. size) based on the present of key
def update_if_present(components, key, value):
    if key in components:
        components[key] = components[key] + value
    else:
        components[key] = value

# generate dictionary of the grouped contents of an apk file
def get_apk_components(apk_file, size_type):
    command = f"{apk_analyzer_path} files list --{size_type} {apk_file}"

    files_with_size_string = execute_command(command)
    files_with_size_list = files_with_size_string.split('\n')

    components = {}

    for item in files_with_size_list:
        size_and_file_name = item.split('\t')

        # this will filter out empty lines and just the lines with size and no file name
        if len(size_and_file_name) == 2 and len(size_and_file_name[1]) > 1:
            size = int(size_and_file_name[0])
            file_name = size_and_file_name[1]

            if file_name == '/lib/arm64-v8a/':
                update_if_present(components, 'Native libraries (arm64-v8a)', size)
            elif file_name.startswith('/classes') and file_name.endswith('.dex'):
                update_if_present(components, 'Classes', size)
            elif file_name == '/resources.arsc' or file_name == '/res/':
                update_if_present(components, 'Resources', size)
            elif file_name == '/assets/':
                update_if_present(components, 'Assets', size)
            elif not file_name.startswith('/lib/') and not file_name.startswith('/classes') and not file_name.startswith('/resources.arsc') and not file_name.startswith('/res/') and not file_name.startswith('/assets/') and not file_name.endswith('/'):
                update_if_present(components, 'Others', size)

    return components

# format bytes to KB or MB
def format_size(size):
    if abs(size) < kb_in_bytes:
        return "0 KB"
    elif abs(size) > mb_in_bytes:
        return f"{round(size / mb_in_bytes, 2)} MB"
    elif abs(size) > kb_in_bytes:
        return f"{round(size / kb_in_bytes, 2)} KB"
    else:
        return f"{size} bytes"

# add an indicator to highlight the size diff
def format_size_with_indicator(size):
    size_indicator = "🔴" if size > kb_in_bytes else "🟢"

    return f"{format_size(size)} {size_indicator}"

# get apk size based on size_type i.e. file-size or download-size
def apk_size(apk_file, size_type):
    command = f"{apk_analyzer_path} apk {size_type} {apk_file}"

    return int(execute_command(command))

# generate html file containing size diff in HRF
def generate_size_diff_html():
    html = "<html>"
    # html += html_head
    html += "<body><h1>Download Size Diff Report</h1><h3>Affected Products</h3>"
    html += "<ul><li><h4><code>release</code></h4><table>"
    html += f"<tr><th>Component</th><th>Base ({apk1Sha})</th><th>Merge ({apk2Sha})</th><th>Diff</th></tr>"

    # print diff of each components of both of the apk files
    for component in set(sorted(set(components1.keys()))) | set(sorted(set(components2.keys()))):
        size1 = components1.get(component, 0)
        size2 = components2.get(component, 0)
        # if size1 != size2:
        html += f"<tr><td>{component}</td><td>{format_size(size1)}</td><td>{format_size(size2)}</td><td>{format_size_with_indicator(size2 - size1)}</td></tr>"

    # calculate size of the apk files
    apk1FileSize = apk_size(apk1Name, 'file-size')
    apk2FileSize = apk_size(apk2Name, 'file-size')
    apk1DownloadSize = apk_size(apk1Name, 'download-size')
    apk2DownloadSize = apk_size(apk2Name, 'download-size')

    # html += f"<tr><td>apk (File Size)</td><td>{format_size(apk1FileSize)}</td><td>{format_size(apk2FileSize)}</td><td>{format_size_with_indicator(apk2FileSize - apk1FileSize)}</td></tr>"
    html += f"<tr><td>apk (Download Size)</td><td>{format_size(apk1DownloadSize)}</td><td>{format_size(apk2DownloadSize)}</td><td>{format_size_with_indicator(apk2DownloadSize - apk1DownloadSize)}</td></tr>"
    html += "</li></ul></table></body></html>"

    with open("apk_size_diff_report.html", "w") as file:
        file.write(html)

apk_analyzer_path = "/usr/local/lib/android/sdk/cmdline-tools/latest/bin/apkanalyzer"

# read arguments passed to this script
apk1Sha = sys.argv[1]
apk2Sha = sys.argv[2]

apk1Name = f"{apk1Sha}.apk"
apk2Name = f"{apk2Sha}.apk"

# generate dictionaries for the apk components size
components1 = get_apk_components(apk1Name, 'download-size')
components2 = get_apk_components(apk2Name, 'download-size')

generate_size_diff_html()