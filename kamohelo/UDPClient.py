from socket import *


host_name = "172.29.38.174"
port_no = 12

cs = socket(AF_INET,SOCK_DGRAM) #UDP essentially man

message = input("Please enter a message neo yaka e ntle : ")
cs.sendto(message.encode(),(host_name , port_no))

modified_message , address = cs.recvfrom(2048)

print(modified_message.decode())


