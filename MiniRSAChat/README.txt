Hello Dear TA!

This is project group: Jie Qi and Jiuzheng Chen!
We proudly present our miniRSA project.
GITHUB: https://github.com/jiuzheng/miniRSA

=========================================================
Project Contributtion:
Jie Qi: RSA algorithm, encryption and decryption in Server and Client
Jiuzheng Chen: Client and Server, brute force decryption

User manual: (You may also use Eclipse to run, remember to add package name miniRSA)
=========================================================
1. "make"
2. "make runServer"
3. "make runClient"
4. Enter the client's keys in server (MUST BE DONE FIRST. So that server accepts connection)
5. Enter the server's keys in client
6. Start chatting
7. If you want to brute force crack the keys, just type in ".brute force"
8. To quit, type ".quit" in both terminals
9. "make clean"

Enjoy!

Approach
=========================================================
1. Create client and server threads to communicate
2. Follow standard RSA algorithm to build encryption and decryption.
3. All messages are transferred in byte, thus use byte array and bit operation to keep information and prevent loss.
4. Use simple brute force to crack and decrypt unknown user's cipher text. 


Sample Runs
=========================================================
Sample runs (hilarious) are included in SampleClientRun.txt and SampleServerRun.txt
Code is pretty straight forward. We tried very hard to make each other understand our codes so we wrote it in a simple and logical way.

THANKS FOR GRADING!
THANKS FOR ALL OF YOUR TIME SPENT ON OUR HOMEWORK THIS SEMESTER!