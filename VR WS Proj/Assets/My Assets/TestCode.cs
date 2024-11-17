using UnityEngine;
using WebSocketSharp;

public class TestCode : MonoBehaviour
{
    WebSocket ws;
    // Start is called before the first frame update
    void Start()
    {
        ws = new WebSocket("ws://localhost:8080"); // initialize web socket
        Debug.Log("WS initialized");
        ws.OnMessage += (sender, e) => // event handler that will print to log when data sent
        {
            Debug.Log("Received data: " + e.Data);
        };
        ws.Connect(); // start web socket connection
        Debug.Log("WS Connected");
        Debug.Log(ws.Ping());
    }

    // Update is called once per frame
    void Update()
    {
        SendData("Working"); // will print in terminal
    }

    public void SendData(string data)
    {
        Debug.Log("sent" + ws.IsAlive); // debug message once every update
        if (ws.IsAlive)
        {
            ws.Send(data); // send to websocket
        }
        else
        {
            Debug.Log("WebSocket is not alive");
        }
    }
}
