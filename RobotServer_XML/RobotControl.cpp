#include <cstdlib>
#include <string.h>	// for bzero()
#include <string>
#include <sstream>
#include <iostream>
#include <fstream>
#include <actinSE/ControlSystem.h>
#include <actinSE/EndEffector.h>
#include <actinSE/CoordinateSystemTransformation.h>
#include <cytonHardwareInterface.h>

using namespace std;
using namespace actinSE;
size_t numJoints = 8;
EcRealVector jointAngle(numJoints, 0.0);
EcRealVector minAngles(numJoints, 0.0);
EcRealVector initAngles(numJoints, 0.0);
EcRealVector maxAngles(numJoints, 0.0);
EcRealVector desiredAngles(numJoints, 0.0);

class RobotControl {
private:
    double radians;
    //size_t numJoints;
    EcReal deltaTime; // 10 seconds
    EcU32 waitTimeInMS;
    bool _readyFlag;
public:
    RobotControl(cyton::hardwareInterface& hardware);
    int startInterface(int argc, char** argv, cyton::hardwareInterface& hardware);
    int startControlSystem();
    bool getReadyFlag();
    void setReadyFlag(bool flag);
    int goToInital(cyton::hardwareInterface& hardware);
    string moveArm(cyton::hardwareInterface& hardware, vector<string> joint);
    void *robotHandler(void* hardware);
    static void *robotHandlerHelper(void *context);
};


///////////////////////////////////////
//Constructor for RobotControl class.//
///////////////////////////////////////
RobotControl::RobotControl(cyton::hardwareInterface& hardware) {
    radians = (3.14 / 180);
    numJoints = 8;
    deltaTime = 5.0; // 10 seconds
    waitTimeInMS = EcU32(2 * deltaTime * 1000);
    _readyFlag = false;

}
//////////////////////////////////////////////////////////////////
///////////////////////////////////////
//test method that is now phased out.//
///////////////////////////////////////
bool RobotControl::getReadyFlag() {
   
    return _readyFlag;
}

void RobotControl::setReadyFlag(bool flag) {
    flag = _readyFlag;
}
////////////////////////////////////////////////////////////////////////////////


////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////
//Methods used to start the drivers for the robot//
//////////////////////////////////////////////////
int RobotControl::startInterface(int argc, char** argv, cyton::hardwareInterface& hardware) {
    if (argc == 2) {

        hardware.setPort("/dev/ttyUSB1");
        cout << "Hardwareportset\n" << endl;
    }

    if (!hardware.init()) {
        std::cerr << "Problem initializing cyton hardwareInterface.\n";
        return -1;
    }

    return 1;

}
int RobotControl::startControlSystem() {

    ControlSystem control;
    if (!control.loadFromFile("/home/bparsons/c++_Programes/RobotServer/RobotServer_XML/include/Cyton_Viewer_4.0.20121011/bin/cyton.ecz")) {
        std::cerr << "Problem loading control system file.\n";
        return -1;
    }

    return 0;
}
////////////////////////////////////////////////////////////////////////////////


////////////////////////////////////
//Moves the robot to home position//
////////////////////////////////////
int RobotControl::goToInital(cyton::hardwareInterface& hardware) {


    hardware.setVelocityFromDeltaTime(deltaTime);

    numJoints = hardware.numJoints(); //******** gets the total number of joints
    if (!numJoints) {
        std::cerr << "Invalid configuration. No joints available.\n";
        return EcFalse;
    }

    std::cout << "Moveing to position 0 " << numJoints << " joints\n";


    // *********** Pull information from configuration,min,max and inital angels *******************************
    std::cout << "Reading angles from configuration.\n";
    if (!hardware.getJointStates(minAngles, cyton::MinAngle)) {
        std::cerr << "Unable to get minimum angles from getJointStates.\n";
        return EcFalse;
    }
    if (!hardware.getJointStates(maxAngles, cyton::MaxAngle)) {
        std::cerr << "Unable to get maximum angles from getJointStates.\n";
        return EcFalse;
    }

    //************ Set all joints to their init value.*******************************
    std::cout << "Setting all joints to init(0) angles.\n";
    if (!hardware.setJointCommands(initAngles) ||
            !hardware.waitUntilCommandFinished(waitTimeInMS)) {
        std::cerr << "Problem setting initialize angles.\n";
        return EcFalse;
    }
    cout << "moved now checking servos" << endl;
    sleep(5); // Pause for a second to visually verify finished cmd.
    desiredAngles = initAngles;

    return 0;

}


///////////////////////////////////////////////////////////////////////////////////
//Method used to take data from the server and send the data to the Robot to move//
///////////////////////////////////////////////////////////////////////////////////
string RobotControl::moveArm(cyton::hardwareInterface& hardware, vector<string> joint) {
    numJoints = hardware.numJoints();
    double angle;

    for (int x = 0; x < numJoints; x++) {
        string str = joint[x];

        angle = atof(str.c_str());

        angle = angle*radians;
        if (x == 7) {
            angle = (angle / radians) / 1000;
            cout << "desired:" << angle << endl;
            cout << "max: " << maxAngles[x] << endl;
            cout << "min: " << minAngles[x] << endl;
        }
        if (angle > maxAngles[x] || angle < minAngles[x]) {
            cout << "error with:" << x << "  at angle" << angle;

            return "Error, set not taken";
        } else {
            desiredAngles[x] = angle;

        }


    }
    if (!hardware.setJointCommands(desiredAngles) ||
            !hardware.waitUntilCommandFinished(waitTimeInMS)) // Let the hardware achieve its position.
    {
        return "Error with movment";
    }


    return "Moved";
}


