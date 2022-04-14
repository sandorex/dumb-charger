import subprocess
from time import sleep

STATE = False

def torch(state):
    subprocess.run([
        r'C:\Users\Sandorex\Downloads\platform-tools\adb.exe',
        'shell',
        "echo",
        str(int(state)),
        '>>',
        '/sys/class/leds/torch/brightness'
        # f"'echo {state} >> /sys/class/leds/torch/brightness'"
    ])

def toggle():
    global STATE

    STATE = not STATE
    torch(STATE)

# POLLING_RATE = 0.2

torch(1)
sleep(0.100)
torch(0)
# sleep(POLLING_RATE)

# data = [
#     1, 0, 0, 1
# ]

# toggle()
# sleep(0.01)

# for i in data:
#     if i >= 1:
#         toggle()
#     sleep(POLLING_RATE)
