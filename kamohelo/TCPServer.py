from socket import *

server_socket = socket(AF_INET , SOCK_STREAM)
server_socket.bind(("",1030)) #bigger door here the general one for all clients

server_socket.listen(1)
print("the server is ready to receive")

while True :
	connection_socket , addr = server_cocket.accept()
	number  =int( connection_socket.recv(2048).decode())
	for i in range(1:number+1):
		x = x + i

	x = str(x)
	connection_socket.send(x.encode())
connection_socket.close()	


