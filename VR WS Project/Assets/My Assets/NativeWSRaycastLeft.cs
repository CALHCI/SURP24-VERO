using System.Collections;
using UnityEngine;
using NativeWebSocket;
using Oculus.Interaction;
using Oculus.Interaction.Surfaces;

public class NativeWSRaycastLeft : MonoBehaviour
{
    public RayInteractor handRay;
    private ISurface plane;
    private WebSocket websocket;
    private bool isConnected = false;

    async void Start()
    {
        plane = this.gameObject.GetComponent<ISurface>(); // Define plane object as canvas
        websocket = new WebSocket("ws://192.168.1.126:8080"); // Initialize WebSocket

        // Event handlers for connection status
        websocket.OnOpen += () =>
        {
            Debug.Log("WebSocket connection opened");
            isConnected = true;
        };
        websocket.OnMessage += (bytes) =>
        {
            Debug.Log("Received data: " + System.Text.Encoding.UTF8.GetString(bytes));
        };
        websocket.OnClose += (e) =>
        {
            Debug.Log("WebSocket connection closed");
            isConnected = false;
        };
        websocket.OnError += (e) =>
        {
            Debug.LogError("WebSocket error: " + e);
            isConnected = false;
        };

        // Start WebSocket connection and wait for it to connect
        await websocket.Connect();
        Debug.Log("Attempting to connect WebSocket...");
        StartCoroutine(WaitForConnection());
    }

    IEnumerator WaitForConnection()
    {
        yield return new WaitUntil(() => websocket.State == WebSocketState.Open);
        Debug.Log("WebSocket Connected");
    }

    void Update()
    {
#if !UNITY_WEBGL || UNITY_EDITOR
        websocket.DispatchMessageQueue(); // Process WebSocket messages on non-WebGL platforms
#endif

        if (!isConnected) return; // Skip update if not connected

        Ray ray = handRay.Ray;
        RaycastHit hitInfo;
        if (Physics.Raycast(ray, out hitInfo, handRay.MaxRayLength))
        {
            Debug.Log("Raycast hit: " + hitInfo.collider.name);
            SendData("Got It");
        }
        else
        {
            Debug.Log("No hit detected.");
        }
    }

    async void SendData(string data)
    {
        if (websocket != null && websocket.State == WebSocketState.Open)
        {
            await websocket.SendText(data);
        }
        else
        {
            Debug.Log("WebSocket is not alive");
        }
    }

    private async void OnApplicationQuit()
    {
        if (websocket != null)
        {
            await websocket.Close();
            Debug.Log("WebSocket closed on quit.");
        }
    }
}
