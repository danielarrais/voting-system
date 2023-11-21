## Amazon EC2
EC2 means **Elastic Compute Cloud** is one of the most popular services of AWS' offering. This service  possibility rent instances of VMs Linux, Windows or Mac OS with diferents configurations, storing data on virtual drives (EBS), scaling the services using an auto-scaling group (ASG).

Knowing EC2 is fundamental to undestand how the Cloud works.

### Configurations options
On moment on launch the EC2 instance we have many options for configure VM:

- The operation System (OS): Linux, Windows or Mac OS
- How much compute power and cores (CPU), random-access memory (RAM)
- How much storage space:
    * Network-attached (EBS and EFS)
    * Hardaware (EC2 Instance Store)
- Network card: speed of the card, public IP address
- Firewall rules: security group
- Bootstrap script (run at first launch): EC2 User Data

#### EC2 User Data
It's a script run as root that's possible to bootstrap your instances on first launch. With the EC2 User Data script we can automate anything you can thin of. For example in below example is maked a script for update my linux, install docker and configure a nginx conteiner for test the access to port 80:
```bash
#! /bin/bash  
  
# Install Docker  
sudo dnf update -y  
sudo dnf install docker -y  
  
# Auto start Docker when starting the machine  
sudo systemctl start docker  
sudo systemctl enable docker  
  
# Start nginx container  
sudo docker run -it --rm -d -p 80:80 --name web nginxdemos/hello
```

With this script after the instance is launched, we can access the nginx conteiner on port 80.