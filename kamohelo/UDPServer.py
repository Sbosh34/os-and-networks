
from socket import *

ss = socket(AF_INET,SOCK_DGRAM)
ss.bind(("",12000))
while True:
	msg , adrr_client = ss.recvfrom(2048)
	ss.sendto(msg.decode().upper().encode() , adrr_client)
	
