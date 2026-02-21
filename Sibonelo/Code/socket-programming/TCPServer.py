from socket import *

serverPort = 33060

serverSocket = socket(AF_INET,SOCK_STREAM)

serverSocket.bind( ('', serverPort))

serverSocket.listen(1)

print("KeoTaba ready to mingle")

while True:
	connectionSocket, addr = serverSocket.accept()

	print("Connection established...")

	sentence = connectionSocket.recv(1024).decode()

	print("Message Decoded")

	moddedSentence = sentence.upper()

	print("Sending message: ", moddedSentence)

	connectionSocket.send( moddedSentence.encode() )

	print("Message sent: Perhaps a Yay! is in order")

	connectionSocket.close()

