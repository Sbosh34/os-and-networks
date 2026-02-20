from socket import *

serverPort = 12000

serverSocket = socket(AF_INET, SOCK_DGRAM)

serverSocket.bind( ('', serverPort))

# Let os tell us the chosen port number so we can use it in the Client side

print('The server on port', serverSocket.getsockname()[1],' is ready to receive')

while True:
	
	message, clientAddress = serverSocket.recvfrom(2048)

	modifiedMessage = message.decode().upper()

	serverSocket.sendto( modifiedMessage.encode(), clientAddress)


	
