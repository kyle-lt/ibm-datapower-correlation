# DataPower Correlation with AppD

## Prerequisites
- Docker Compose 3.x
- MacOS (Docker Desktop for Mac)

	> __Note:__  This project makes use of host.docker.internal IP address (hence the MacOS req)
				I really didn't spend time to make this flexible, but you could totally do so :)

## Workflow

The flow is between two Spring Boot MVC Reactive RestController apps (Client and Server), with DataPower as a gateway in between.

Client --> DataPower --> Server

**Client**

Client is exposed on http://host.docker.internal:8081, and calling it invokes an HTTP call to DataPower

**DataPower**

The DataPower gateway is listening http://host.docker.internal:9943, which is where the Client call is directed

**Server**

The server is listening on http://host.docker.internal:8080. It receives the call from DataPower and responds.


The response is a simple String `Server Response!`, which can be seen in a browser or cURL output



## How To Run

This isn't quite as easy as it *should be*, but here we go!

### DataPower Container

First, let's get the DataPower container up and running.

```bash
$ cd src
$ docker run -it \
  -v $PWD/config:/drouter/config \
  -v $PWD/local:/drouter/local \
  -e DATAPOWER_ACCEPT_LICENSE=true \
  -e DATAPOWER_INTERACTIVE=true \
  -p 9090:9090 \
  -p 9943:9943 \
  --name datapower \
  ibmcom/datapower
20201201T231428.190Z [0x8040006b][system][notice] logging target(default-log): Logging started.
--- MORE LOG OUTPUT ---
20201201T231432.699Z [0x00350014][mgmt][notice] quota-enforcement-server(QuotaEnforcementServer): tid(831): Operational state up
```

Don't give up, the above will likely take 10-20 seconds w/o any status before the login prompt shows up

```bash
login: admin
Password: ***** (admin)

Welcome to IBM DataPower Gateway console configuration. 
Copyright IBM Corporation 1999, 2020 

Version: IDG.10.0.1.0 build 325431 on Sep 9, 2020 9:25:27 PM
Delivery type: LTS
Serial number: 0000001

idg# configure; web-mgmt 0 9090;
Global mode
Web management: successfully started
```

[Hit Enter]

```bash
idg(config)# 20201201T231555.954Z [0x8100003f][mgmt][notice] domain(default): tid(303): Domain configuration has been modified.
20201201T231555.955Z [0x00350014][mgmt][notice] web-mgmt(WebGUI-Settings): tid(303): Operational state up
```

Ok, so now the DataPower process is running, and we've enabled the management interface.  Keep the shell open or the container will die.


### DataPower Web Management GUI

Navigate to the Web Management GUI and login.  

**Default URL is http://host.docker.internal:9090**
**Default Credentials are admin/admin**

![Web Management GUI Login](/README_IMAGES/DataPower_Login.png)

It uses a self-signed SSL cert, so use Firefox (or some browser that'll let you in over broken SSL).

https://host.docker.internal:9090/

Import the Multi-Protocol Gateway config
- Click on Import Configuration (near the bottom of the screen)
- Choose xml, and navigate to /src/config/appd-test/export.xml
- Click next, click import

We're done with DataPower!

	> __Note:__  If you need to change the destination IP address (default is http://host.docker.internal:8080) - do that NOW!
				Also, don't touch the port, leave that at 8080 or you'll need to go change it numerous places.  Just leave it pls.

### Client and Server Java Apps

Open up a new shell and navigate to the `src` directory.

Copy the `.env_public` file to a file named `.env`, and populate accordingly

	> __Note:__  You must fill out the AppD Java agent environment variables, the others can remain as defaults if you are running on MacOS

These are images on Docker Hub, so just start with Docker Compose and it'll pull the images and you'll be off and running

```bash
$ docker-compose up -d
```

### Administer Load

Navigate to the client in a browser, by default, http://host.docker.internal:8081

You should see the string `Server Response!`

Now go to AppD and check out the App Flow Map - the call correlates through DataPower as if it weren't even there!

### Correlate, but also show DataPower on the Flow Map

Ok, so, OOB, AppD just ignores this gateway.  In order to visualize the two legs of this transaction, and the DataPower Gateway, we need to coax
the instrumentation to play nice.

// TODO - custom-correlation-activity.xml



  