from socket import *

serverName = '196.47.192.199'

serverPort = 50000

clientSocket = socket(AF_INET, SOCK_STREAM)

print('Initiating connection...')
clientSocket.connect( (serverName, serverPort) )

sentence = input('Input lowercase sentence: ')

clientSocket.send(sentence.encode())

modifiedSentence = clientSocket.recv(1024)

print("Raw Bytes: ",modifiedSentence)
print("From server: ", modifiedSentence.decode(errors='replace') )

clientSocket.close()
