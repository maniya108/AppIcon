var exec = require("cordova/exec");
var PLUGIN_NAME = "AppIcon";

exports.isSupported = function () {
  return new Promise((resolve, reject) => {
    exec(resolve, reject, PLUGIN_NAME, "isSupported", []);
  });
};

exports.getName = function () {
  return new Promise((resolve, reject) => {
    exec(
      (response) => {
        try {
          const result =
            Object.keys(response).length === 0 ? "" : response.value;
          resolve(result);
        } catch (e) {
          reject("Error parsing getName response: " + e.message);
        }
      },
      reject,
      PLUGIN_NAME,
      "getName",
      []
    );
  });
};


exports.change = function (options) {
  return new Promise((resolve, reject) => {
    exec(resolve, reject, PLUGIN_NAME, "change", [options]);
  });
};

exports.reset = function (options) {
  return new Promise((resolve, reject) => {
    exec(resolve, reject, PLUGIN_NAME, "reset", [options]);
  });
};
