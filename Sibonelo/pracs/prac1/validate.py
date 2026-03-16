# validate.py
#
# Check answer format is correct and split into separate files for the automatic marker.
#
# Answer file format:
# #q1
# <Trace frame number>
# <Source IP>
# <Source port>
#
# #q2
# <Trace frame number>
# <Destination IP>
# <Destination port>
# 
# #q3
# <Trace frame number>
# <Sequence number>
# 
# #q4
# <Trace frame number>
# <Sequence number>
# <Acknowledgement number>
# 
# #q5
# <Trace frame number>
# <Sequence number>
# 
# #q6
# <Trace frame number for 1st segment>
# <Time first segment sent>
# <Time ACK received>
# <RTT>
# <Second segment RTT>
# <Estimated RTT>
#
# #q7
# <Length of first>
# <Length of second>
# <Length of third>
# <length of fourth>

# #q8
# <Minimum advertised buffer space>
# 
#  #q10
#  <Trace frame number>
#  <IP Address>
#
# #q11
# <TTL value>
#
# #q12
# <Protocol value>
#
# #q13
# <header length>
# <body length>
#
# #q14
# <Fragmented>
#
import sys
import traceback


def validate_as_frame(string):
    return string.isdigit()


def validate_as_ip_addr(string):
    parts = string.split('.')
    if not len(parts)==4:
        return False
    else:
        for part in parts:
            if not part.isdigit() or int(part)>255:
                return False
        return True


def strip_leading_zeroes(string):
    return str(int(string))


def clean_up_ip_addr(string):
    parts = string.split('.')
    return strip_leading_zeroes(parts[0])+'.'+strip_leading_zeroes(parts[1])+'.'+strip_leading_zeroes(parts[2])+'.'+strip_leading_zeroes(parts[3])

def validate_as_port(string):
    return string.isdigit() and int(string) < 65536

def validate_as_ttl(string):
    return string.isdigit() and int(string) <  256

def validate_as_protocol(string):
    return string.isdigit() and int(string) < 65536

def validate_as_seq_num(string):
    return string.isdigit() and int(string)<pow(2, 32)

def validate_as_length(string):
    return string.isdigit() and int(string) < 65536

def validate_as_payload_size(string):
    return string.isdigit() and int(string) < 65536

def validate_as_header_size(string):
    return string.isdigit() and int(string) < 61

def validate_as_time(string):
    parts = string.split('.')
    return len(parts) == 2 and parts[0].isdigit() and parts[1].isdigit()

def clean_up_time(string):
    parts = string.split('.')
    return strip_leading_zeroes(parts[0])+'.'+parts[1]

def is_comment(line):
    line = line.strip()
    return line != '' and line[0] == '#'


def is_blank(line):
    return line.strip() == ''


def get_answers(filename):
    contents = []
    answer_indices = []
    
    file = open(filename, 'r')
    for line in file:
        if not is_comment(line) and not is_blank(line):
            answer_indices.append(len(contents)) 
        contents.append(line.strip())
    file.close()
    
    return contents, answer_indices


def filename(question):
    return question+'.'+extension

extension = 'out'

error_message = 'One or more data values are of an invalid type.'

def writefile(question, answers, valid_data=False):
    if valid_data:
        print('OK')
    else:
        answers = [error_message]
    if write_flag:
        file = open(filename(question), 'w')
        for item in answers:
            file.write(item+'\n')
        file.close()

write_flag = False

REQUIRED_ANSWERS = 31


def main():
    try:
        global write_flag

        # Pull in answer filename from command line
        if len(sys.argv) < 2 or len(sys.argv) > 3:
            print("python validate [-s] <answer filename>")
            return
        elif len(sys.argv) == 2:
            in_file, answer_indices = get_answers(sys.argv[1])
        elif sys.argv[1] == '-s':
            write_flag = True
            in_file, answer_indices = get_answers(sys.argv[2])
        else:
            print("python validate [-s] <answer filename>")
            return

        if not len(answer_indices)==REQUIRED_ANSWERS:
            print("Incorrect number of data values (answers) in the file '{}'. Require {}, but read {}.".format(sys.argv[1], REQUIRED_ANSWERS, len(answer_indices)))
            return

        print('Checking answer file contains correct quantity and type of answer values.')
        fails = 0

        # Question One: extract '<frame>, <ip address>, <port>
        question = 'one'
        print('Expecting a frame number, IP address and port number for question {}.'.format(question))
        valid_data = True
        answer_index = 3
        answers = []
        if not validate_as_frame(in_file[answer_indices[answer_index-3]]):
            print('- Frame number is not valid (line {}).'.format(answer_indices[answer_index-3]+1))
            valid_data = False
            fails = fails+1
        if not validate_as_ip_addr(in_file[answer_indices[answer_index-2]]):
            print('- IP address is not valid (line {}).'.format(answer_indices[answer_index-2]+1))
            valid_data = False
            fails = fails+1
        else:
            answers.append(clean_up_ip_addr(in_file[answer_indices[answer_index-2]]))
            
        if not validate_as_port(in_file[answer_indices[answer_index-1]]):
            print('- Port number is not valid (line {}).'.format(answer_indices[answer_index-1]+1))
            valid_data = False
            fails = fails+1
        else:
            answers.append(strip_leading_zeroes(in_file[answer_indices[answer_index-1]]))
                        
        writefile(question, answers, valid_data)

        # Question Two: extract '<frame>, <ip address>, <port>'
        question = 'two'
        print('Expecting a frame number, IP address and port number for question {}.'.format(question))
        valid_data = True
        answer_index = answer_index+3
        answers=[]
        if not validate_as_frame(in_file[answer_indices[answer_index-3]]):
            valid_data = False
            fails = fails+1

        if not validate_as_ip_addr(in_file[answer_indices[answer_index-2]]):
            print('- IP address is not valid (line {}).'.format(answer_indices[answer_index-2]+1))
            valid_data = False
            fails = fails+1
        else:
            answers.append(clean_up_ip_addr(in_file[answer_indices[answer_index-2]]))
            
        if not validate_as_port(in_file[answer_indices[answer_index-1]]):
            print('- Port number is not valid (line {}).'.format(answer_indices[answer_index-1]+1))
            valid_data = False
            fails = fails+1
        else:
            answers.append(strip_leading_zeroes(in_file[answer_indices[answer_index-1]]))
            
        writefile(question, answers, valid_data)

        # Question Three: extract '<frame>, <sequence number>'
        question = 'three'
        print('Expecting a frame number and sequence number for question {}.'.format(question))
        valid_data = True
        answer_index = answer_index+2
        answers=[]
        if not validate_as_frame(in_file[answer_indices[answer_index-2]]):
            print('- Frame number is not valid (line {}).'.format(answer_indices[answer_index-2]+1))
            valid_data = False
            fails = fails+1

        if not validate_as_seq_num(in_file[answer_indices[answer_index-1]]):
            print('- Sequence number is not valid (line {}).'.format(answer_indices[answer_index-1]+1))
            valid_data = False
            fails = fails+1
        else:
            answers.append(strip_leading_zeroes(in_file[answer_indices[answer_index-1]]))
            
        writefile(question, answers, valid_data)

        # Question Four: extract '<frame>, <sequence number>, <ack number>'
        question = 'four'
        print('Expecting a frame number and two sequence numbers for question {}.'.format(question))
        valid_data = True
        answer_index = answer_index+3
        answers=[]
        if not validate_as_frame(in_file[answer_indices[answer_index-3]]):
            print('- Frame number is not valid (line {}).'.format(answer_indices[answer_index-3]+1))
            valid_data = False
            fails = fails+1

        if not validate_as_seq_num(in_file[answer_indices[answer_index-2]]):
            print('- 1st sequence number is not valid (line {}).'.format(answer_indices[answer_index-2]+1))
            valid_data = False
            fails = fails+1
        else:
            answers.append(strip_leading_zeroes(in_file[answer_indices[answer_index-2]]))

        if not validate_as_seq_num(in_file[answer_indices[answer_index-1]]):
            print('- 2nd sequence number is not valid (line {}).'.format(answer_indices[answer_index-1]+1))
            valid_data = False
            fails = fails+1
        else:
            answers.append(strip_leading_zeroes(in_file[answer_indices[answer_index-1]]))
            
        writefile(question, answers, valid_data)

        # Question Five: extract '<frame>, <sequence number>'
        question = 'five'
        print('Expecting a frame number and sequence number for question {}.'.format(question))
        answer_index = answer_index + 2
        valid_data = True
        answers=[]
        if not validate_as_frame(in_file[answer_indices[answer_index-2]]):
            print('- Frame number is not valid (line {}).'.format(answer_indices[answer_index-2]+1))
            valid_data = False
            fails = fails+1

        if not validate_as_seq_num(in_file[answer_indices[answer_index-1]]):
            print('- Sequence number is not valid (line {}).'.format(answer_indices[answer_index-1]+1))
            valid_data = False
            fails = fails+1
        else:
            answers.append(strip_leading_zeroes(in_file[answer_indices[answer_index-1]]))
        
        writefile(question, answers, valid_data)

        # Question Six <frame number> <Time first segment sent> <Time ACK received> <RTT> <Second segment RTT> <Estimated RTT>
        question = 'six'
        print('Expecting a frame number, two timestamps and three RTT values for question {}.'.format(question))
        valid_data = True
        answers=[]

        answer_index = answer_index+6
        # <Trace frame number for 1st segment>
        if not validate_as_frame(in_file[answer_indices[answer_index-6]]):
            print('- Frame number is not valid (line {}).'.format(answer_indices[answer_index-6]+1))
            valid_data = False
            fails = fails+1

        labels = ['timestamp', 'timestamp', '1st RTT', '2nd RTT', '3rd RTT']
        for index in range(5):
            if not validate_as_time(in_file[answer_indices[answer_index-5+index]]):
                print('- The {} value is not valid. (line {}).'.format(labels[index], answer_indices[answer_index-5+index]+1))
                valid_data = False
                fails = fails+1
            else:
                answers.append(clean_up_time(in_file[answer_indices[answer_index-5+index]]))

        writefile(question, answers, valid_data)

        # Question Seven: [<frame number> from 6] <Length of first> <Length of second> <Length of third> <Length of fourth]
        question = 'seven'
        print('Expecting four segment lengths for question {}.'.format(question))
        answer_index = answer_index+4
        valid_data = True
        answers=[]

        labels = ['first', 'second', 'third', 'fourth']
        for index in range(0, 4):
            if not (validate_as_length(in_file[answer_indices[answer_index-4+index]])):
                print('- The {} value is not valid. (line {}).'.format(labels[index+1], answer_indices[answer_index-4+index]+1))
                valid_data = False
                fails = fails+1
            else:
                answers.append(strip_leading_zeroes(in_file[answer_indices[answer_index-4+index]]))

        writefile(question, answers, valid_data)

        # Question Eight: [<frame number. from 6] <Minimum advertised buffer space>
        question = 'eight'
        print('Expecting a buffer size for question {}.'.format(question))
        valid_data = True
        answer_index = answer_index+1
        answers = []

        if not in_file[answer_indices[answer_index-1]].isdigit():
            print('Buffer size for question eight is not valid (line {}).'.format(answer_indices[answer_index-1]+1))
            valid_data = False
            fails = fails+1
        else:
            answers.append(strip_leading_zeroes(in_file[answer_indices[answer_index-1]]))
            
        writefile(question, answers, valid_data)

        # Question Ten: extract '<frame>, <ip address>'
        question = 'ten'
        print('Expecting a frame number and IP address for question {}.'.format(question))
        valid_data = True
        answer_index = answer_index+2
        answers = []
        if not validate_as_frame(in_file[answer_indices[answer_index-2]]):
            print('- Frame number is not valid (line {}).'.format(answer_indices[answer_index-2]+1))
            valid_data = False
            fails = fails+1

        if not validate_as_ip_addr(in_file[answer_indices[answer_index-1]]):
            print('IP address is not valid (line {}).'.format(answer_indices[answer_index-1]+1))
            valid_data = False
            fails = fails+1
        else:
            answers.append(clean_up_ip_addr(in_file[answer_indices[answer_index-1]]))
            
        writefile(question, answers, valid_data)

        # Question Eleven:[<frame number> from 10] TTL
        question = 'eleven'
        print('Expecting a TTL value for question {}.'.format(question))
        valid_data = True
        answer_index = answer_index+1
        answers = []

        if not validate_as_ttl(in_file[answer_indices[answer_index-1]]):
            print('- TTL value is not valid (line {}).'.format(answer_indices[answer_index-1]+1))
            valid_data = False
            fails = fails+1
        else:
            answers.append(strip_leading_zeroes(in_file[answer_indices[answer_index-1]]))

        writefile(question, answers, valid_data)

        # Question Twelve: [<frame number> from 10] <Protocol value>
        question = 'twelve'
        print('Expecting a protocol value for question {}.'.format(question))
        valid_data = True
        answer_index = answer_index+1
        answers = []

        if not validate_as_protocol(in_file[answer_indices[answer_index-1]]):
            print('- Protocol value is not valid (line {}).'.format(answer_indices[answer_index-1]+1))
            valid_data = False
            fails = fails+1
        else:
            answers.append(strip_leading_zeroes(in_file[answer_indices[answer_index-1]]))

        writefile(question, answers, valid_data)

        # Question thirteen: [<frame number from 10] <header length> <body length>
        question = 'thirteen'
        print('Expecting a header size and payload size for question {}.'.format(question))
        valid_data = True
        answer_index = answer_index+2
        answers = []

        if not validate_as_header_size(in_file[answer_indices[answer_index-2]]):
            print('- Header size is not valid (line {}).'.format(answer_indices[answer_index-2]+1))
            valid_data = False
            fails = fails+1
        else:
            answers.append(strip_leading_zeroes(in_file[answer_indices[answer_index-2]]))

        if not validate_as_payload_size(in_file[answer_indices[answer_index-1]]):
            print('- Payload size is not valid (line {}).'.format(answer_indices[answer_index-1]+1))
            valid_data = False
            fails = fails+1
        else:
            answers.append(strip_leading_zeroes(in_file[answer_indices[answer_index-1]]))

        writefile(question, answers, valid_data)

        # Question fourteen: Fragmentation yes/no
        question = 'fourteen'
        print('Expecting a yes/no response for question {}.'.format(question))
        valid_data = True
        answer_index = answer_index+1
        answers = []

        response = in_file[answer_indices[answer_index-1]].lower()

        if not (response == 'yes' or response == 'no'):
            print('- Response is not valid (line {}).'.format(answer_indices[answer_index-1]+1))
            valid_data = False
            fails = fails+1
        else:
            answers.append(response)

        writefile(question, answers, valid_data)

        if fails>0:
            if fails==1:
                verb = 'is'
            else:
                verb = 'are'
            print('Your file contains the correct number of responses but {} {} not the correct type. These will fail automatic marking.'.format(fails, verb))

    except FileNotFoundError:
        print('Failed to open answer/module file.')
    except Exception as e:
        print('Unexpected error:', e)

    
if __name__=='__main__':
    main()
