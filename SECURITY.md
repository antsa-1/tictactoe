# Security Policy
26.1.2022
## Supported Versions


| Version | Supported          |
| ------- | ------------------ |
| All     | :white_check_mark: |


## Reporting a Vulnerability

Interested hearing about all possible bugs/threats related to code. Especially security and (backend) multithreading issues. Backend validates user input data.
* Traffic is in localhost plain http which in production environment should be changed to TLS even though there are no sensitive information involved (except password).
* WebSockets can use TLS (wss -protocol) if site has domain name with SSL certificate.
* Self signed certifictes are not supported by common browsers and are not used.
* Applications login and registration functionalities are delayed on purpose. In theory a WAF could be configured in front the REST-api functionalities.

Issues can be created in GitHub issues tab, all are appreciated.

Site is not currently deployed to any cloud environment by me since used cloud provider's free time ended.
