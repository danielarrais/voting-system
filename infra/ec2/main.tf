# Create SSH key pair
resource "aws_key_pair" "tf-created-ssh-key" {
  key_name   = "tf-created-ssh-key"
  public_key = file("~/.ssh/ec2.pub")
}

resource "aws_kms_key" "a" {
  description             = "KMS key 1"
  deletion_window_in_days = 10
}

# Create security group
resource "aws_security_group" "tf-created-security-group-to-ec2" {
  name        = "tf-created-security-group-to-ec2"
  description = "Allow SSH and HTTP"

  # Default rules
  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

# Attach rule to enable access to port 22
resource "aws_security_group_rule" "tf-created-security-group-rule-ssh" {
  security_group_id = aws_security_group.tf-created-security-group-to-ec2.id
  type        = "ingress"
  to_port     = 22
  from_port   = 22
  protocol    = "tcp"
  cidr_blocks = ["0.0.0.0/0"]
  depends_on = [
    aws_security_group.tf-created-security-group-to-ec2
  ]
}

# Attach rule to enable access to port 80
resource "aws_security_group_rule" "tf-created-security-group-rule-http" {
  security_group_id = aws_security_group.tf-created-security-group-to-ec2.id
  type        = "ingress"
  to_port     = 80
  from_port   = 80
  protocol    = "tcp"
  cidr_blocks = ["0.0.0.0/0"]
  depends_on = [
    aws_security_group.tf-created-security-group-to-ec2
  ]
}

# Create EC2 instance
resource "aws_instance" "tf-created-ec2" {
  ami             = "ami-01bc990364452ab3e"
  instance_type   = "t2.micro"
  key_name        = aws_key_pair.tf-created-ssh-key.key_name
  security_groups = [aws_security_group.tf-created-security-group-to-ec2.name]
  user_data       = file("./02. ec2/user-data.sh")

  ebs_block_device {
    device_name = "/dev/xvda"
    volume_size = 30
    volume_type = "gp3"
  }

  tags = {
    Name = "tf-created"
  }

  depends_on = [
    aws_security_group.tf-created-security-group-to-ec2
  ]
}