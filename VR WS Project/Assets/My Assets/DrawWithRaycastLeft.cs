using UnityEngine;
using Oculus.Interaction;
using Oculus.Interaction.Surfaces;
using WebSocketSharp;
using System;

public class DrawWithRaycastLeft : MonoBehaviour
{
    public RayInteractor handRay;
    public Renderer planeRenderer;
    private Vector3 bottomCorner;
    WebSocket ws;

    // Start is called before the first frame update
    void Start()
    {

        bottomCorner = planeRenderer.bounds.min;

        ws = new WebSocket("ws://10.144.232.48:8080"); // initialize web socket
        Debug.Log("WS initialized");
        ws.OnMessage += (sender, e) => // event handler that will print to log when data sent
        {
            Debug.Log("Received data: " + e.Data);
        };
        ws.Connect(); // start web socket connection
        Debug.Log("WS Connected");
        if (ws.IsAlive)
        {
            Debug.Log("ALIVEEEE");
        }
    }

    // Update is called once per frame
    void Update()
    {
        Ray ray = handRay.Ray;
        RaycastHit hitInfo;
        if (Physics.Raycast(ray, out hitInfo, handRay.MaxRayLength))
        {
            SendData("(" + (hitInfo.point.x - bottomCorner.x) + ", " + (hitInfo.point.y - bottomCorner.y) + ")");
        } // use and update this code a little bit to check if the raycast hit is being registered by the plane
        else
        {
            Debug.Log("No data");
        }

    }

    public void SendData(string data)
    {
        Debug.Log(data);
        if (ws.IsAlive)
        {
            Debug.Log("ws alive");
            ws.Send(data);
        }
        else
        {
            Debug.Log("WebSocket is not alive");
        }
    }
    void OnDestroy()
    {
        ws.Close();
    }
}
