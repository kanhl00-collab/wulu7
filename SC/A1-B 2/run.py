from server import WebServer

PORT = 8080
with WebServer("./", PORT) as httpd:
    httpd.start()


