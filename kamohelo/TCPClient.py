from socket import *

serverName = "172.29.38.174"
port_no = 1030

client_socket = socket(AF_INET ,SOCK_STREAM) #TCP NOW CAUSE I AM USING SOCK_STREAM
client_socket.connect((serverName , port_no))
num = input("Please enter an interger neo yaka e ntle hlk : ")

client_socket.send(num.encode())

num_final =int( client_socket.recv(2048).decode() )
print(num_final)

client_socket.close()

#close the socket 




 
