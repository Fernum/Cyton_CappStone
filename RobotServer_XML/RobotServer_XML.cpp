#include <cstdlib>
#include <sys/socket.h>	// for socket(), bind(), listen(), accept()
#include <netinet/in.h>	// for PF_INET, SOCK_STREAM, IPPROTO_TCP 
#include <stdio.h>	// for printf(), perror()
#include <unistd.h>	// for read(), write(), close()
#include <string.h>	// for bzero()
#include <string>
#include <sstream>
#include <iostream>
#include <fstream>
#include <queue>
#include <pthread.h>
#include <rapidxml-1.13/rapidxml_print.hpp>
#include <rapidxml-1.13/rapidxml.hpp>
#include <rapidxml-1.13/rapidxml_utils.hpp>
#include <cytonHardwareInterface.h>
#include "RobotControl.cpp"

using namespace std;
using namespace actinSE;
using namespace rapidxml;

#define DEFAULT_PORT        2694
#define DEFAULT_BUFFER      1024
#define VERBOSE 1
pthread_mutex_t mutex1 = PTHREAD_MUTEX_INITIALIZER;
pthread_mutex_t mutex2 = PTHREAD_MUTEX_INITIALIZER;

int createSocket();
int bindSocket(const int);
int listenSocket(const int);
int acceptSocket(const int);
int readSocket(int, char *);
int writeSocket(int, char *);
//////////////////////////////////////////////////////////
//Set of data to be passed into the robotcontrol thread///
//////////////////////////////////////////////////////////
struct robotData {
    char* pqBuffer;
    cyton::hardwareInterface* hardware;
    RobotControl* rbot1;
    cyton::hardwareInterface* hardware2;
    RobotControl* rbot2;

    int *argc1;
    char *argv1[];

};


///////////////////////////////////////////////////////
//Rapidxml Parsing to pars the xml packet into vectors//
///////////////////////////////////////////////////////
void xmlStringParser(char* text, vector<string>& r1vect, vector<string>& r2vect) {
    try {
        // ...

        rapidxml::xml_document<> doc;
        doc.parse < parse_validate_closing_tags > (text);
        cout << "Parsed Document" << endl;
        // vector<string> svect(8);
        xml_node<>* robotData = doc.first_node("RobotData");
        xml_node<>* robot = robotData->first_node("Robot");

        while (robot != NULL) {
            string robotID = robot->first_attribute("ID")->value();
            cout << "Robot:" << robotID << endl;
            cout << "" << endl;
            xml_node<>* joint = robot->first_node("Joint");
            while (joint != NULL) {
                string sjoint = joint->first_attribute("ID")->value();
                string svalue = joint->first_attribute("Value")->value();
                if (robotID == "0") {
                    r1vect[atoi(sjoint.c_str())] = svalue;

                } else if (robotID == "1") {
                    r2vect[atoi(sjoint.c_str())] = svalue;
                }
                cout << "Joint:" << sjoint << endl;
                cout << "Value:" << svalue << endl;
                joint = joint->next_sibling("Joint");
            }
            robot = robot->next_sibling("Robot");
        }
        cout << "end of while loop" << endl;

    } catch (...) {
        cout << "Error: data was malformed" << endl;
    }

}

/////////////////////////////////////////////////////////////////////
//Test method to read the vectors after being created by the parser//
/////////////////////////////////////////////////////////////////////
int readVector(vector<string> vread) {
    if (vread.size() > 0) {
        for (int x = 0; x < vread.size(); x++) {
            cout << "vector" << vread[ x ] << endl;
        }
    } else {
        cout << "error with vector" << endl;
        return -1;
    }
    return 0;
}


/////////////////////////////////////////////////////////////////////////////////////
//Reads the current packet in the buffer and trims the message down to correct size//
//Size is determineed by the first 4 bytes of the buffer/packet                    //
/////////////////////////////////////////////////////////////////////////////////////
void readPacket(char temp[], vector<string>& r1Vect, vector<string>& r2Vect) {
    int dataSize = 0;
    char dataTemp[4];


    for (int j = 0; j < 4; j++) {
        dataTemp[j] = temp[j];
    }

    dataSize = atoi(dataTemp);
    char fullMessage[dataSize];


    for (int j = 0; j < dataSize; j++) {
        fullMessage[j] = temp[4 + j];
    }
    fullMessage[dataSize - 1] = '\0';

    cout << "message Converted" << endl;
    string sStream(fullMessage);
    cout << sStream << endl;
    //////// XML String parser////////////////////////       
    xmlStringParser(fullMessage, r1Vect, r2Vect);
}


///////////////////////////////
//Creates socket to be binded//
///////////////////////////////
int createSocket() {
    int sock = socket(AF_INET, SOCK_STREAM, 0);
    if (sock < 0) {
        perror("Opening Socket.");
        return -1;
    }
    int r = bindSocket(sock);
    if (r == -1)return r;
    r = listenSocket(sock);
    if (r == -1)return r;

    return sock;
}

///////////////////////////////////////////////////////////////////////////////
//Binds socket to specified address created by global vaiable for port number//
//                      DEFAULT_PORT is the global vairable                  //
/////////////////////////////////////////////////////////////////////////////// 
int bindSocket(const int sock) {
    struct sockaddr_in self;
    socklen_t nlen = sizeof self;
    bzero(&self, sizeof self);
    self.sin_family = AF_INET;
    self.sin_port = htons(DEFAULT_PORT);
    self.sin_addr.s_addr = htonl(INADDR_ANY);

    if (bind(sock, (sockaddr *) & self, nlen) < 0) {
        perror("Bind to Socket Failed.");
        return -1;
    }
    if (VERBOSE) {
        printf("Socket Binding Successful.\n");
    }
}

///////////////////////////////////////////
// Listen on port for socket connections.//  
///////////////////////////////////////////
int listenSocket(const int sock) {
    if (listen(sock, 5) < 0) {
        perror("Failed Listening to Socket.");
        return -1;
    }
    if (VERBOSE) {
        printf("\nListening on Socket Port: %d\n", DEFAULT_PORT);
    }
}

///////////////////////////////////////////
// Accepts socket connection from client.//
///////////////////////////////////////////
int acceptSocket(const int sock) {
    sockaddr_in peer;
    socklen_t plen = sizeof peer;
    bzero(&peer, plen);

    if (VERBOSE) {
        printf("Server Waiting...\n\n");
    }
    int client = accept(sock, (sockaddr *) & peer, &plen);
    if (client < 0) {
        perror("Socket Accepting Error.");
    }
}

//////////////////////////////////////
// Reads in packets from the client.//
//////////////////////////////////////
int readSocket(int client, char* buffer) {
    int check = 0;
    ssize_t read_bytes;
    if ((read_bytes = recv(client, buffer, DEFAULT_BUFFER, 0)) < 0) {
        perror("Read Socket Error.");
        close(client);
        return -1;
    } else if (read_bytes == 0) {
        return 0;
    }


    //buffer[read_bytes] = '\0';
    //if (VERBOSE) { printf("Received: Size=%lu\n[%s]\n", read_bytes, buffer); }
    cout << "message found" << endl;
    string sStream2(buffer);
    cout << "Full Message:" << sStream2 << endl;
    return read_bytes;
}

///////////////////////////
// Writes data to client.//
///////////////////////////
int writeSocket(int client, char *buffer) {
    ssize_t wrote_bytes;
    if ((wrote_bytes = send(client, buffer, strlen(buffer), 0)) < 0) {
        perror("Write Socket Error.");
        close(client);
        return -1;
    } else if (wrote_bytes == 0) {
        return 0;
    }

    buffer[wrote_bytes] = '\0';
    if (VERBOSE) {
        printf("Transmitted: Size=%lu\n[%s]\n", wrote_bytes, buffer);
    }

    return wrote_bytes;
}
///////////////////////////////////////////////////////////////
//              Void method used in the threads.             //
//This method calls all move functions for the robotic arm.  //
//This method takes in the struck robotdata for void hardware//
///////////////////////////////////////////////////////////////

void *robotHandler(void* hardware) {

    //pthread_mutex_lock(&mutex1);
    struct robotData* rdata;
    rdata = (struct robotData*) hardware;
    char* temp;
    temp = rdata->pqBuffer;
    int r1 = 0, r2 = 1; /* Robot IDs */
    vector<string> r1Vect(8), r2Vect(8); /* Joint Vector */
    readPacket(temp, r1Vect, r2Vect);
    rdata->rbot1->moveArm(*rdata->hardware, r1Vect);
    //pthread_mutex_unlock(&mutex1);

}

/////////////////////////////////////////////////////////
//Main method that runs the server and robot controls.///
/////////////////////////////////////////////////////////

int main(int argc, char *argv[]) {
    priority_queue<char *> message;
    int it1 = 5;
    int socket, client;
    pthread_t thread1, thread2;

    //robot1.startInterface(argc,argv);

    robotData rdata;



    pthread_attr_t attr;
    int flag = -10;
    int flag2 = -10;
    void *ref;
    const EcString pluginName = "include/Cyton_Viewer_4.0.20121011/bin/cytonPlugin";
    const EcString pluginName2 = "include/Cyton_Viewer_4.0.20121011/bin/cytonPlugin2";
    const EcString pluginConfig1 = "cytonConfig.xml";

    char sbuffer[1024];
    sbuffer[0] = 'm';
    sbuffer[1] = 'o';
    sbuffer[2] = 'v';
    sbuffer[3] = 'e';
    sbuffer[4] = 'd';

    cyton::hardwareInterface hardwareTemp1(pluginName, pluginConfig1);
    RobotControl robot1(hardwareTemp1);
    rdata.hardware = &hardwareTemp1;
    flag = robot1.startControlSystem();
    flag = robot1.startInterface(argc, argv, *rdata.hardware);
    int results = -10;
    if (flag > 0) {
        rdata.rbot1 = &robot1;
        rdata.rbot1->setReadyFlag(true);
        robot1.goToInital(*rdata.hardware);
        
    }



   



    int sock = socket = createSocket();
    if (sock == -1) {
        perror("Failed Creating Socket.");
        return 0;
    }
    client = acceptSocket(socket);

    //if (flag >0 ) {
    for (;;) {
        char buffer[DEFAULT_BUFFER];
        rdata.pqBuffer = buffer;
        int size = readSocket(client, rdata.pqBuffer);
        if (size <= 0) {
            cout << "Erorr.Client disonected" << endl;
            break;
        }
        //pthread_mutex_lock(&mutex1);
        cout << "data added to queue" << endl;
        //pthread_mutex_unlock(&mutex1);
        pthread_attr_init(&attr);
        int iret1 = pthread_create(&thread1, NULL, robotHandler, (void*) &rdata);

        pthread_join(thread1, &ref);
        writeSocket(client, sbuffer);
    }

    //}
}