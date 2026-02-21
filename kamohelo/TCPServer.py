from socket import *

server_socket = socket(AF_INET , SOCK_STREAM)
server_socket.bind(("",33060)) #bigger door here the general one for all clients

server_socket.listen(1)
print("the server is ready to receive")

while True :
	connection_socket , addr = server_socket.accept()
	print("Message has been received for real")
	msg  =connection_socket.recv(2048).decode().upper()
	print("we are sending :", msg)
	connection_socket.send(msg.encode())
connection_socket.close()	


