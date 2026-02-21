from socket import *

serverName = "196.47.192.199"
port_no = 33060

client_socket = socket(AF_INET ,SOCK_STREAM) #TCP NOW CAUSE I AM USING SOCK_STREAM
client_socket.connect((serverName , port_no))
num = input("Please enter a Message BRO : ")

client_socket.send(num.encode())

num_final = client_socket.recv(2048).decode() 
print(num_final)

client_socket.close()

#close the socket 




 
