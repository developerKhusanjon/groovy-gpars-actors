package actors

@Grab("org.codehaus.gpars:gpars:1.2.1")

import groovyx.gpars.actor.Actor
import groovyx.gpars.actor.DefaultActor

class Mailer extends DefaultActor {
    int maxLoad
    int minLoad

    void afterStart() {
        maxLoad = 400
        minLoad = 10
    }

    void act() {
        loop {
            react { int load, String address ->
                if (load > maxLoad) {
                    println "your parcel weights $load, it is too heavy load for single mail!"
                    println "max load is $maxLoad"
                    reply 'too heavy'
                }
                else if (load < minLoad) {
                    println "your parcel weights $load, it is too light load for single mail!"
                    println "min load is $minLoad"
                    reply 'too light'
                }
                else {
                    println "your parcel weights $load and it is sent successfully to $address."
                    reply 'sent successfully'
                    terminate()
                }
            }
        }
    }
}

class Sender extends DefaultActor {
    String mailAddress
    Actor server
    int parcel

    void act() {
        loop {
            parcel = new Random().nextInt(1000) + 1
            server.send parcel
            react {
                switch (it) {
                    case 'too heavy': println "$mailAddress: $parcel was too heavy"; break
                    case 'too light': println "$mailAddress: $parcel was too light"; break
                    case 'sent successfully': println "$mailAddress: $parcel was sent, thank you!"; terminate(); break
                }
            }
        }
    }
}

def mailer = new Mailer().start()
def sender = new Sender(mailAddress: 'Andidzan, Markhamat, Yuksalish - 7', server: mailer).start()

//this forces main thread to live until both actors stop
[mailer, sender]*.join()