Name: Ronak UpadhyayaUSC ID: 2287766736Email: rdupadhy@usc.eduLecture Session Number: 29909RThe Java src is divided into two packages: Game and Network.
1. The Game package contains the Card, Deck, Game, GameMessage and Player classes.
a) Card - Contains enumerations for Suit and Rank
b) Deck - Contains methods for shuffling, add and drawing Card objects
c) Game - Contains a vector of Player objects, stores the dealer’s hand and contains methods for interacting with the Game object
d) GameMessage - Contains enumerations for the types of messages the client-server network can communicate
e) Player - Contains values for current bet, number of chips, cards and contains methods for interacting with the Player object
2. The Network package contains the GameClient, GameServer and GameServerThread classes.
a) GameClient - It extends Thread and contains methods for reading and writing message
b) GameServer - Contains Game objects, GameServerThread objects and contains methods for message redirection logic
c) GameServerThread - It extends Thread and contains methods for reading and writing message. Each GameServerThread object corresponds to a GameClient.

Note: The Utility package is redundant. It was used for testing purposes only. 
The program must be started by running GameServer and GameClient.