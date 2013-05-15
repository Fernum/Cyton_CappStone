HARDWARE_NAME = cytonHardwareExample
HARDWARE_SRC  = examples/$(HARDWARE_NAME).cpp
HARDWARE_OBJ  = $(HARDWARE_SRC:.cpp=.o)

CONTROL_NAME = cytonControlExample
CONTROL_SRC  = examples/$(CONTROL_NAME).cpp
CONTROL_OBJ  = $(CONTROL_SRC:.cpp=.o)

SPECTEST_NAME = cytonSpecTestRoutines
SPECTEST_SRC  = examples/$(SPECTEST_NAME).cpp
SPECTEST_OBJ  = $(SPECTEST_SRC:.cpp=.o)

BIN_DIR = ../bin

CXXFLAGS = -Wall -O2 -I../include

LDFLAGS = -L../lib -Wl,-rpath,../lib -lecFoundCore -lecCytonHardwareInterface

.SUFFIXES: .cpp

default: $(BIN_DIR)/$(HARDWARE_NAME) $(BIN_DIR)/$(CONTROL_NAME) $(BIN_DIR)/$(SPECTEST_NAME)

all: default

.cpp.o:
	$(CXX) -c $< $(CXXFLAGS) -o $@

$(BIN_DIR)/$(HARDWARE_NAME): $(HARDWARE_OBJ)
	$(CXX) $< $(LDFLAGS) -o $@

$(BIN_DIR)/$(CONTROL_NAME): $(CONTROL_OBJ)
	$(CXX) $< $(LDFLAGS) -lecActinSE -o $@

$(BIN_DIR)/$(SPECTEST_NAME): $(SPECTEST_OBJ)
	$(CXX) $< $(LDFLAGS) -lecActinSE -o $@

clean:
	rm -f $(BIN_DIR)/$(HARDWARE_NAME)   $(HARDWARE_OBJ)
	rm -f $(BIN_DIR)/$(CONTROL_NAME)    $(CONTROL_OBJ)
	rm -f $(BIN_DIR)/$(SPECTEST_NAME)   $(SPECTEST_OBJ)

