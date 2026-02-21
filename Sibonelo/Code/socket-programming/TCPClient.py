from socket import *

serverName = '196.47.192.199'

serverPort = 33060

clientSocket = socket(AF_INET, SOCK_STREAM)

print('Initiating connection...')
clientSocket.connect( (serverName, serverPort) )

sentence = input('Input lowercase sentence: ')

clientSocket.send(sentence.encode())

modifiedSentence = clientSocket.recv(1024)

print("From server: ", modifiedSentence.decode() )

clientSocket.close()
