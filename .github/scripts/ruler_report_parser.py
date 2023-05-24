import sys
import json

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

def read_report_file(file_path):
    with open(file_path, 'r') as file:
        json_data = json.load(file)
    return json_data

# add/update the value (i.e. size) based on the present of key
def update_if_present(components, key, value):
    if key in components:
        components[key] = components[key] + value
    else:
        components[key] = value

# generate dictionary of the grouped contents of an apk file
def get_apk_components(json_data):
    components = {}

    for component in json_data['components']:
        for file in component['files']:
            if file['name'].startswith('/lib/'):
                update_if_present(components, 'Native libraries', file['downloadSize'])
            elif file['name'].startswith('/resources.arsc') or file['name'].startswith('/res/'):
                update_if_present(components, 'Resources', file['downloadSize'])
            elif file['name'].startswith('/assets/'):
                update_if_present(components, 'Assets', file['downloadSize'])
            elif file['name'].startswith('/'):
                update_if_present(components, 'Others', file['downloadSize'])
            else:
                update_if_present(components, 'Classes', file['downloadSize'])

    return components

def format_size(size):
    kb_in_bytes = 1024
    mb_in_bytes = 1024 * 1024

    if size == 0:
        return "0 KB"
    elif abs(size) > mb_in_bytes:
        return f"{round(size / mb_in_bytes, 2)} MB"
    elif abs(size) > kb_in_bytes:
        return f"{round(size / kb_in_bytes, 2)} KB"
    else:
        return f"{size} bytes"

def format_size_with_indicator(size):
    size_indicator = "🔴" if size > 0 else "🟢"

    return f"{format_size(size)} {size_indicator}"

# generate html file containing size diff in HRF
def generate_size_diff_html():
    html = "<html>"
    # html += html_head
    html += "<body><h1>Raw Size Report</h1><h3>Affected Products</h3>"
    html += "<ul><li><h4><code>release</code></h4><table>"
    html += f"<tr><th>Component</th><th>Base ({apk1Sha})</th><th>Merge ({apk2Sha})</th><th>Diff</th></tr>"

    # print diff of each components of both of the apk files
    for component in set(sorted(set(components1.keys()))) | set(sorted(set(components2.keys()))):
        size1 = components1.get(component, 0)
        size2 = components2.get(component, 0)
        # if size1 != size2:
        html += f"<tr><td>{component}</td><td>{format_size(size1)}</td><td>{format_size(size2)}</td><td>{format_size_with_indicator(size2 - size1)}</td></tr>"

    apk1DownloadSize = apk1Json['downloadSize']
    apk2DownloadSize = apk2Json['downloadSize']

    html += f"<tr><td>apk</td><td>{format_size(apk1DownloadSize)}</td><td>{format_size(apk2DownloadSize)}</td><td>{format_size_with_indicator(apk2DownloadSize - apk1DownloadSize)}</td></tr>"
    html += "</li></ul></table></body></html>"

    with open("apk_size_diff_report.html", "w") as file:
        file.write(html)

# read arguments passed to this script
apk1Sha = sys.argv[1]
apk2Sha = sys.argv[2]

apk1Name = f"{apk1Sha}.json"
apk2Name = f"{apk2Sha}.json"

apk1Json = read_report_file(apk1Name)
apk2Json = read_report_file(apk2Name)

# generate dictionaries for the apk components size
components1 = get_apk_components(apk1Json)
components2 = get_apk_components(apk2Json)

generate_size_diff_html()