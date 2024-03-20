# PhishingBlockServer

This is the authentication server for the voice phishing prevention application **Phishing Block**.

## Installation
Before running, add the corresponding yml file in `src/auth-service/src/main/resources`.
```yml
spring:
  mail:
    host: smtp.gmail.com
    port: 587
    username: {YOUR_MAIL}
    password: {YOUR_MAIL_APP_PASSWORD}
    properties:
      mail:
        smtp:
          auth: true
          starttls:
            enable: true
            required: true
        connectiontimeout: 5000
        timeout: 5000
        writetimeout: 5000
    auth-code-expiration-millis: 300000
```

move `scripts/` and start `scripts/env-start.sh`

```bash
#!/bin/sh

docker-compose down
docker-compose up -d
```

After then, start `src/auth-service/src/main/java/com/phishing/authservice/AuthServiceApplication`
