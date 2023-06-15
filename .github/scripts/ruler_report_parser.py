import sys
import json

def read_report_file(file_path):
    with open(file_path, 'r') as file:
        json_data = json.load(file)
    return json_data

# generate dictionary of the grouped contents of an apk file
def get_apk_components(json_data):
    components = {}

    for component in json_data['components']:
        for file in component['files']:
            if file['name'].startswith('/lib/'):
                update_if_present(components, 'Native libraries (arm64-v8a)', file['downloadSize'])
            elif file['name'].startswith('/resources.arsc') or file['name'].startswith('/res/'):
                update_if_present(components, 'Resources', file['downloadSize'])
            elif file['name'].startswith('/assets/'):
                update_if_present(components, 'Assets', file['downloadSize'])
            elif file['name'].startswith('/'):
                update_if_present(components, 'Others', file['downloadSize'])
            else:
                update_if_present(components, 'Classes', file['downloadSize'])

    return components

# add/update the value (i.e. size) based on the present of key
def update_if_present(components, key, value):
    if key in components:
        components[key] = components[key] + value
    else:
        components[key] = value

def format_size(size):
    if abs(size) < kb_in_bytes:
        return "0 KB"
    elif abs(size) > mb_in_bytes:
        return f"{round(size / mb_in_bytes, 2)} MB"
    elif abs(size) > kb_in_bytes:
        return f"{round(size / kb_in_bytes, 2)} KB"
    else:
        return f"{size} bytes"

def format_size_with_indicator(size):
    size_indicator = "🔴" if size > kb_in_bytes else "🟢"

    return f"{format_size(size)} {size_indicator}"

# generate html file containing size diff in HRF
def generate_size_diff_html():
    html = "<html>"
    html += "<body><h1>Ruler Size Diff Report</h1><h3>Affected Products</h3>"
    html += "<ul><li><h4><code>release</code></h4><table>"
    html += f"<tr><th>Component</th><th>Base ({apk_1_sha})</th><th>Merge ({apk_2_sha})</th><th>Diff</th></tr>"

    # print diff of each components of both of the apk files
    for component in set(sorted(set(components_1.keys()))) | set(sorted(set(components_2.keys()))):
        size_1 = components_1.get(component, 0)
        size_2 = components_2.get(component, 0)
        html += f"<tr><td>{component}</td><td>{format_size(size_1)}</td><td>{format_size(size_2)}</td><td>{format_size_with_indicator(size_2 - size_1)}</td></tr>"

    # calculate size of the apk files
    apk_1_download_size = apk_1_json['downloadSize']
    apk_2_download_size = apk_2_json['downloadSize']

    html += f"<tr><td>apk (Download Size)</td><td>{format_size(apk_1_download_size)}</td><td>{format_size(apk_2_download_size)}</td><td>{format_size_with_indicator(apk_2_download_size - apk_1_download_size)}</td></tr>"
    html += "</li></ul></table></body></html>"

    with open("ruler_diff_report.html", "w") as file:
        file.write(html)

kb_in_bytes = 1024
mb_in_bytes = 1024 * 1024

# read arguments passed to this script
apk_1_sha = sys.argv[1]
apk_2_sha = sys.argv[2]

apk_1_json = read_report_file(f"{apk_1_sha}.json")
apk_2_json = read_report_file(f"{apk_2_sha}.json")

# generate dictionaries for the apk components size
components_1 = get_apk_components(apk_1_json)
components_2 = get_apk_components(apk_2_json)

generate_size_diff_html()