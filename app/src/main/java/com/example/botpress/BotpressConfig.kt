package com.example.botpress

object BotpressConfig {
    const val API_KEY = "bp_bak_QqnQLElVKXc6F-NFmSvoZ0uWSf2DmMFBv2Qj"
    const val BOT_ID = "fe8bdfa4-828b-43c3-b69e-994bca8c586c"
    const val INJECT_SCRIPT_URL = "https://cdn.botpress.cloud/webchat/v3.7/inject.js"
    const val CONFIG_SCRIPT_URL = "https://files.bpcontent.cloud/2026/09/30/17/20260930172524-5LEJNRW6.js"

    fun generateHtml(scanContextMessage: String = ""): String {
        val safeMessage = scanContextMessage.replace("\\", "\\\\")
            .replace("\"", "\\\"")
            .replace("\n", "\\n")

        return """
            <!DOCTYPE html>
            <html lang="en">
            <head>
              <meta charset="UTF-8">
              <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
              <title>EcoLeaf Assistant</title>
              <style>
                * { box-sizing: border-box; }
                html, body {
                  margin: 0;
                  padding: 0;
                  width: 100%;
                  height: 100%;
                  overflow: hidden;
                  background-color: #FBF9F5;
                  font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, Helvetica, Arial, sans-serif;
                }
                #loading-container {
                  display: flex;
                  flex-direction: column;
                  align-items: center;
                  justify-content: center;
                  height: 100%;
                  width: 100%;
                  color: #5E2750;
                  text-align: center;
                  padding: 24px;
                }
                .spinner {
                  width: 40px;
                  height: 40px;
                  border: 3.5px solid #F3EAF1;
                  border-top: 3.5px solid #5E2750;
                  border-radius: 50%;
                  animation: spin 0.8s linear infinite;
                  margin-bottom: 16px;
                }
                @keyframes spin {
                  0% { transform: rotate(0deg); }
                  100% { transform: rotate(360deg); }
                }
                .bp-widget-widget, #bp-web-widget-container, .bpw-layout {
                  width: 100% !important;
                  height: 100% !important;
                  max-height: 100% !important;
                  max-width: 100% !important;
                  position: absolute !important;
                  top: 0 !important;
                  left: 0 !important;
                  right: 0 !important;
                  bottom: 0 !important;
                  border-radius: 0 !important;
                }
                .bpw-chat-container {
                  height: 100% !important;
                }
              </style>
              <script src="$INJECT_SCRIPT_URL"></script>
              <script src="$CONFIG_SCRIPT_URL" defer></script>
            </head>
            <body>
              <div id="loading-container">
                <div class="spinner"></div>
                <div style="font-weight: bold; font-size: 16px; margin-bottom: 6px;">Connecting to Sprig AI</div>
                <div style="font-size: 13px; color: #656A76;">Powered by Botpress Assistant</div>
              </div>

              <script>
                var scanContextSent = false;
                var scanContextMsg = "$safeMessage";

                function openAndConnect() {
                  if (window.botpress) {
                    var loader = document.getElementById('loading-container');
                    if (loader) loader.style.display = 'none';

                    if (typeof window.botpress.open === 'function') {
                      window.botpress.open();
                    }

                    if (scanContextMsg && scanContextMsg.length > 0 && !scanContextSent) {
                      scanContextSent = true;
                      setTimeout(function() {
                        if (typeof window.botpress.sendMessage === 'function') {
                          window.botpress.sendMessage(scanContextMsg);
                        }
                      }, 1000);
                    }
                  }
                }

                window.addEventListener('load', function() {
                  var attempts = 0;
                  var interval = setInterval(function() {
                    attempts++;
                    if (window.botpress) {
                      openAndConnect();
                      clearInterval(interval);
                    } else if (attempts > 50) {
                      clearInterval(interval);
                    }
                  }, 250);
                });
              </script>
            </body>
            </html>
        """.trimIndent()
    }
}
