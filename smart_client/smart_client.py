 # Yilin Dong
 # V00928413

import socket
import sys
import re
import ssl 
def main():
    https = False
    http = False
    if len(sys.argv) != 2:
        print("INVALID INPUT")
        exit()
    print('\nWebsite: ' + sys.argv[1])
    if(check_format(sys.argv[1]) == False):
        pass
    else:
        print("please input a valid websit")
        exit()
    parsed = parse(sys.argv[1])
    path, host = breakintopath(parsed)
    try:    
        s = ssl.wrap_socket(socket.socket(socket.AF_INET, socket.SOCK_STREAM)) # wrap the socket
        s.connect((host,443))
        print("support of HTTPS: YES")
        https = True
    except Exception as e:
        print("Support of HTTPS: NO")  # Not support HTTPS
        https = False
    try:
        s = socket.socket(socket.AF_INET,socket.SOCK_STREAM)
        s.connect((host,80))
        print("support of HTTP: YES")
        http = True

    except Exception as e:
        print("Support of HTTP: NO")  # Not support HTTPS
        http = False
    try:
        context = get_http2_ssl_context()
        connection = socket.create_connection((host, 443))
        negotiate_tls(connection, context, host) 
        support_http2 = True
        print("support of HTTP2: YES")
    except Exception as e:
        support_http2 = False
        print("support of HTTP2: No")
    if https:
        print('Connecting over port 443...\n')
        https_port(host,path,443)
    elif http:
        print('Connecting over port 80...\n')
        https_port(host,path,80)
    elif https == False and http == False:
        print("neither https or http works, please try another website")
        exit()
# connect to the host and read the header for cookies and status
# input: str host : host name,  str path: path name , int port: port 443 or 80
#output : null
def https_port(host,path,port):
    redirect = False
    location = ""
    rec = ""
    count = 0
    password = False
    rec_di = ""
    s = ssl.wrap_socket(socket.socket(socket.AF_INET, socket.SOCK_STREAM)) 
    s.connect((host,port))
    if path != None:
        try:
            check = s.send(bytes('GET /%s HTTP/1.1\r\nHost: %s\r\n\r\n' % (path, host), 'utf8'))
            if check == 0:
                print ("failed connecting to the path, please try another url")
            data = s.recv(10000)
            rec = rec + bytes.decode(data)
        except Exception as e:
            sock.close()              
    else:
        try: 
            check =  s.sendall(bytes('GET / HTTP/1.1\r\nHost: ' + host + '\r\n\r\n',
                                   'utf8'))
            if check == 0:
                print ("failed connecting to the host, please try another url")
            data = s.recv(10000)
            rec = rec + bytes.decode(data)
    
        except Exception as e:
            print ("failed connecting to the host plus path ")         
    count = 0
    rec_di = rec
    while 1:
        index = rec.find('\n')
        if index == -1:
            info = rec
        else:
            info = rec[:index]
        if len(info) == 0 or info[0] == '\r':
            break
        if info.find('HTTP') == 0:
            status = info[9:12]
            if status == '505':  # Check status code
                print("Status code: " + status + " - HTTP version not supported")
            if status == '404':
                print("Status code: " + status + " - File not found")
            if status == '301':
                print("Status code: " + status + " - Moved permanently")
                new_l = rec_di.split('Location',1)[1]
                new_l2 = new_l.split('\n',1)[0]
                new_path, new_host = breakintopath(parse(new_l2))
                print("Redirecting to new Location:" +  new_host)

                https_port(new_host,new_path,port)
                break
            if status == '302':
                print("Status code: " + status + " - Found")
            
                new_l = rec_di.split('Location',1)[1]
                new_l2 = new_l.split('\n',1)[0]
                new_path, new_host = breakintopath(parse(new_l2))
                print("Redirecting to new Location:" +  new_host)
                https_port(new_host,new_path,port)
                break

            if status  == '401':
                print("Status code: " + status + " - Found")
                password = True
            if status == '200':
                print('Status code: ' + status + ' - OK')
            else:
                print('Status code: ' + status)
        if info.find('Set-Cookie') == 0: 
            cookie = info[12:]
            index2 = cookie.find('=')
            key = cookie[:index2]  # Get Set-Cookie key
            cookie = cookie[index2 + 1:]
            index3 = cookie.find(';')
            domain = ''  # domain value
            if index3 != -1:
                index4 = cookie.find('domain=')  # Get domain
                if index4 != -1:
                    domain = cookie[index4 + 7:]
            if count == 0:  # First time received this Set-Cookie
                print('List of Cookies:	')
                count = count + 1
            if domain == '':
                domain = host
            print('* name: ' + key + ', domain name: ' + domain)
        if index == -1:  # String end, break
            break
        rec = rec[index + 1:]  # Delete this row
    if password == True:
        print("The web is password-protected")
    else :
        print("The web is NOT password-protected")
# get the http2's ssl context
# input: null
# output : http2's ssl context

def get_http2_ssl_context():
    context = ssl.create_default_context(purpose=ssl.Purpose.SERVER_AUTH)

    context.options |= (
        ssl.OP_NO_SSLv2 | ssl.OP_NO_SSLv3 | ssl.OP_NO_TLSv1 | ssl.OP_NO_TLSv1_1
    )

    context.options |= ssl.OP_NO_COMPRESSION

    context.set_ciphers("ECDHE+AESGCM:ECDHE+CHACHA20:DHE+AESGCM:DHE+CHACHA20")
    context.set_alpn_protocols(["h2", "http/1.1"])

    try:
        context.set_npn_protocols(["h2", "http/1.1"])
    except NotImplementedError:
        pass

    return context
# check if the url negotiates HTTP2
#input tcp_conn socket connection, context: http2 context, str host: host name
#output the tls connection (if occurs exception, then the main function would catch it, if not then it supports http2
# so the output is not important )

def negotiate_tls(tcp_conn, context, host):
    tls = context.wrap_socket(tcp_conn, server_hostname=host)

    negotiated_protocol = tls.selected_alpn_protocol()
    if negotiated_protocol is None:
        negotiated_protocol = tls.selected_npn_protocol()

    if negotiated_protocol != "h2":
        raise RuntimeError("Didn't negotiate HTTP/2!")

    return tls
    
    # input: web string
    # return bool
    # check if the string is a url or not
    # return false if it's a string 
    # return true if it is
def check_format(web): # check if the input is in the valid formati
    # resource: https://stackoverflow.com/questions/827557/how-do-you-validate-a-url-with-a-regular-expression-in-python
    regex = re.compile(
        r'(?:https?:\/\/)?'
        r'(www.)?' 
        r'(?:(?:[A-Z0-9](?:[A-Z0-9-]{0,61}[A-Z0-9])?\.)+[A-Z]{2,6}\.?|'  # domain...
        r'localhost|'  # localhost...
        r'\d{1,3}\.\d{1,3}\.\d{1,3}\.\d{1,3})' # ...or ip
        r'(?::\d+)?'  # optional port
        r'(?:/?|[/?]\S+)$', re.IGNORECASE)
    return regex.search(web) == None
# break the string into protocol and the rest 
# input : string web 
#output string 
def parse(web):
    substring = ""
    if web.find("https://") != -1 :
        split_string = web.split("https://",1)
        return         split_string[1]
    elif web.find("http://") != -1 :
        split_string = web.split("http://",1)
        return         split_string[1]
    else:
        return web
# break the string into the path and host 
#input : string url without protocol 
#ouput string, string
def breakintopath(web):
    if web.find("/") != -1 :
        split = web.split("/",1)
        return (split[1], split[0] )

    return None, web
if __name__ == "__main__":
    main()
