#include <ESP8266WiFi.h>

#define D5 14

#ifndef STASSID
#define STASSID ""
#define STAPSK  ""
#endif
const char* ssid = STASSID;
const char* password = STAPSK;

WiFiServer server(80);

void setup() {
  Serial.begin(115200);

  pinMode(D5, OUTPUT);
  digitalWrite(D5, 0);

  Serial.print(F("Connecting to "));
  Serial.println(ssid);

  WiFi.mode(WIFI_STA);
  WiFi.begin(ssid, password);
  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(F("."));
  }
  Serial.println(F("\r\nWiFi connected"));

  server.begin(); // Start the server
  Serial.println(F("Server started"));

  Serial.print(F("IP: "));
  Serial.println(WiFi.localIP());
}

void loop() {
  // Check if a client has connected
  WiFiClient client = server.available();
  if (!client) {
    return;
  }
  Serial.println(F("## new client"));

  client.setTimeout(5000); // default is 1000

  // Read the first line of the request
  String req = client.readStringUntil('\r');
  Serial.println(F("request: "));
  Serial.println(req);

  // Match the request
  int val;
  if (req.indexOf(F("/io/0")) != -1) {
    val = 0;
    Serial.println(F("ok: OFF"));
  } else if (req.indexOf(F("/io/1")) != -1) {
    val = 1;
    Serial.println(F("ok: ON"));
  } else {
    val = digitalRead(D5);
    Serial.println(F("error: invalid request!!"));
  }
  digitalWrite(D5, val);

  // read/ignore the rest of the request
  // do not client.flush(): it is for output only, see below
  while (client.available()) {
    // byte by byte is not very efficient
    client.read();
  }

  // Send the response to the client
  client.print(F("HTTP/1.1 200 OK\r\nContent-Type: text/html\r\n\r\n<!DOCTYPE HTML>\r\n"));

  client.print(F("<html>"));

  client.print(F("<p><a href='http://"));
  client.print(WiFi.localIP());
  client.print(F("/io/1'>ON</a></p>"));

  client.print(F("<p><a href='http://"));
  client.print(WiFi.localIP());
  client.print(F("/io/0'>OFF</a></p>"));

  client.print(F("</html>"));

  Serial.println(F("Disconnecting from client."));
}
