import http.server, socketserver, urllib.parse, os
from io import StringIO
import sys
from jinja2 import Template

class TemplateEngine:
    def render(self, template_path, context):
        with open(template_path, 'r') as f:
            src = f.read()

        pos = 0
        code = []
        base_indent = "    "
        code.append("def __render_func__():")
        code.append(f"{base_indent}output = []")

        add_html_indent = False  # Tracks whether we're between a `:` and next `<%`

        while pos < len(src):
            start = src.find("<%", pos)

            # Flush remaining HTML
            if start == -1:
                html = src[pos:]
                if html:
                    for line in html.splitlines(True):
                        indent = base_indent + ("    " if add_html_indent else "")
                        code.append(f"{indent}output.append({repr(line)})")
                break

            # Emit HTML before this tag
            html = src[pos:start]
            if html:
                for line in html.splitlines(True):
                    indent = base_indent + ("    " if add_html_indent else "")
                    code.append(f"{indent}output.append({repr(line)})")

            # Process the tag
            if src.startswith("<%=", start):
                end = src.find("%>", start)
                if end == -1:
                    raise SyntaxError("Unclosed <%= ... %>")
                expr = src[start + 3:end].strip()
                indent = base_indent + ("    " if add_html_indent else "")
                code.append(f"{indent}output.append(str({expr}))")
                pos = end + 2
            else:
                end = src.find("%>", start)
                if end == -1:
                    raise SyntaxError("Unclosed <% ... %>")
                stmt = src[start + 2:end].strip()
                code.append(f"{base_indent}{stmt}")

                # Activate HTML indent if this is a block (ends with :)
                add_html_indent = stmt.endswith(":")
                pos = end + 2

        code.append(f"{base_indent}return ''.join(output)")
        code.append("result = __render_func__()")
        #print(code)

        local_vars = dict(context)
        exec("\n".join(code), local_vars)
        return local_vars["result"]


class WebServer:
    def __init__(self, directory, port):
        self.directory = directory
        self.port = port

    def __enter__(self):
        os.chdir(self.directory)
        handler = self.make_handler()
        self.httpd = socketserver.TCPServer(("", self.port), handler)
        return self

    def __exit__(self, *args):
        self.httpd.server_close()

    def make_handler(self):
        engine = TemplateEngine()

        class CustomHandler(http.server.SimpleHTTPRequestHandler):
            def do_GET(self):
                parsed = urllib.parse.urlparse(self.path)
                path = parsed.path
                query = urllib.parse.parse_qs(parsed.query)

                if path == "/":
                    self.send_response(200)
                    self.end_headers()
                    self.wfile.write(engine.render("index.html", {}).encode())

                elif path == "/hello":
                    message = query.get("message", [""])[0]
                    self.send_response(200)
                    self.end_headers()
                    self.wfile.write(engine.render("hello.html", {"message": message}).encode())

                else:
                    super().do_GET()

            def do_POST(self):
                if self.path == "/whatsup":
                    length = int(self.headers['Content-Length'])
                    post_data = urllib.parse.parse_qs(self.rfile.read(length).decode())
                    info = post_data.get("info", [""])[0]
                    self.send_response(200)
                    self.end_headers()
                    self.wfile.write(engine.render("whatsup.html", {"info": info}).encode())
        return CustomHandler

    def start(self):
        print(f"Serving at http://localhost:{self.port}")
        self.httpd.serve_forever()

