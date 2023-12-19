## Steps

1. Start kafka environment

2. For Start Angular:
 - Run `npm i`
 - Run `npm run start`
 
3. For Build:
 - Run `npm build`

3. For Spring Boot:
 - Run Application Class

# To work SSE in the back of nginx we have to add following lines in nginx.conf file

```
Added extra lines: 


        proxy_set_header Upgrade $http_upgrade;
        proxy_set_header Connection 'upgrade';
        proxy_cache_bypass $http_upgrade;

        proxy_cache off;  # Turn off caching
        proxy_buffering off;  # Turn off proxy buffering
        chunked_transfer_encoding on;  # Turn on chunked transfer encoding
        tcp_nopush on;  # Turn on TCP NOPUSH option, disable Nagle algorithm
        tcp_nodelay on;  # Turn on TCP NODELAY option, disable delay ACK algorithm
        keepalive_timeout 300;  # Set keep-alive timeout to 65 seconds
```
