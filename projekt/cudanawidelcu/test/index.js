const express = require("express");
const Eureka = require("eureka-js-client").Eureka;
const axios = require("axios");

const app = express();
const port = 1234;

const client = new Eureka({
  instance: {
    app: "test-service",
    hostName: "localhost",
    ipAddr: "127.0.0.1",
    statusPageUrl: "http://localhost:1234/info",
    port: {
      $: 1234,
      "@enabled": "true",
    },
    vipAddress: "localhost",
    dataCenterInfo: {
      "@class": "com.netflix.appinfo.InstanceInfo$DefaultDataCenterInfo",
      name: "MyOwn",
    },
  },
  eureka: {
    host: "localhost",
    port: 1111,
    servicePath: "/eureka/apps/",
  },
});

app.listen(port, () => {
  console.log(`Serwer uruchomiony na http://localhost:${port}`);

  client.start((error) => {
    if (error) {
      console.log("Błąd podczas rejestracji w Eureka:", error);
    } else {
      console.log("Pomyślnie zarejestrowano w Eureka");
      const instances = client.getInstancesByAppId("images-service");
      console.log(instances);

    //   // Make request
    //   axios
    //     .get("http://images-service:3000")
    //     // Show response data
    //     .then((res) => console.log(res.data))
    //     .catch((err) => console.log(err));
    }
  });
});

process.on("SIGINT", () => {
  client.stop((error) => {
    if (error) {
      console.log("Błąd podczas wyrejestrowania z Eureka:", error);
    } else {
      console.log("Pomyślnie wyrejestrowano z Eureka");
    }
    process.exit();
  });
});
