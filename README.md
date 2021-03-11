# socketUDP
Repository that provides Java UDP Socket Plus N service. Client sends an int M and server replies with the resulting sum of N + M , beeing N another int specified by
the server.
Server execution: Java plusd server_port N
Client execution: Java plus server_ip server_port M timeout

The server_ip implemented is only localhost ip. Timeout is the time specified in seconds that you wait until you wait for the acknowlede of server.
