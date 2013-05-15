CONTROL_NAME = controlExample
CONTROL_SRC  = examples/$(CONTROL_NAME)/controlExample.cpp
CONTROL_OBJ  = $(CONTROL_SRC:.cpp=.o)

DESCRIBE_NAME = describeExample
DESCRIBE_SRC  = examples/describe/main.cpp
DESCRIBE_OBJ  = $(DESCRIBE_SRC:.cpp=.o)

REMOTE_NAME = remoteTester
REMOTE_SRC  = examples/remoteTester/remoteTester.cpp
REMOTE_OBJ  = $(REMOTE_SRC:.cpp=.o)

BIN_DIR   = ../bin

CXXFLAGS =  -I../include

LDFLAGS=  -L../lib -Wl,-rpath,../lib -lecFoundCore -lecActinSE

.SUFFIXES: .cpp

default: $(BIN_DIR)/$(CONTROL_NAME) $(BIN_DIR)/$(DESCRIBE_NAME) $(BIN_DIR)/$(REMOTE_NAME)

all: default

.cpp.o:
	$(CXX) -c $< -o $@ $(CXXFLAGS)

default all: $(BIN_DIR)/$(CONTROL_NAME) $(BIN_DIR)/$(DESCRIBE_NAME) $(BIN_DIR)/$(REMOTE_NAME)

$(BIN_DIR)/$(CONTROL_NAME): $(CONTROL_OBJ)
	$(CXX) $< $(LDFLAGS) -o $@

$(BIN_DIR)/$(DESCRIBE_NAME): $(DESCRIBE_OBJ)
	$(CXX) $< $(LDFLAGS) -o $@

$(BIN_DIR)/$(REMOTE_NAME): $(REMOTE_OBJ)
	$(CXX) $< $(LDFLAGS) -lecRemoteCommands -o $@

clean:
	rm -f $(BIN_DIR)/$(CONTROL_NAME) $(CONTROL_OBJ) $(BIN_DIR)/$(DESCRIBE_NAME) $(DESCRIBE_OBJ) $(BIN_DIR)/$(REMOTE_NAME) $(REMOTE_OBJ)
