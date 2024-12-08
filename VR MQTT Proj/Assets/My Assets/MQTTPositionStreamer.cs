using UnityEngine;
using System.Net;
using System.Text;
using System.Collections;
using uPLibrary.Networking.M2Mqtt;
using Oculus.Interaction;

public class MQTTPositionStreamer : MonoBehaviour
{

    private MqttClient client;
    private const string Topic = "vr";
    private bool isPublishing = true; // Flag to control publishing
    private const float interval = 1f;
    private int publishAccum = 0;

    public RayInteractor leftRay;
    public RayInteractor rightRay;
    public MeshCollider planeCollider;
    public GameObject cube;
    private Vector3 colliderCenter;

    void Start()
    {
        // Broker address and port
        string brokerAddress = "broker.hivemq.com";
        int port = 1883;

        // Get the broker's IP address
        IPAddress brokerIpAddress = Dns.GetHostAddresses(brokerAddress)[0];

        // Find bottom corner of plane
        colliderCenter = planeCollider.bounds.center;
        Debug.Log("Start Create Client");
        
        //Create and connect the MQTT client
        client = new MqttClient(brokerIpAddress, port, false, null, null, MqttSslProtocols.None);
        string clientId = "UnityTestClient_" + Random.Range(0, 1000);
        Debug.Log("Created Client---");

        try
        {
            client.Connect(clientId);
            if (client.IsConnected)
            {
                Debug.Log("Connected to MQTT broker successfully.");
                //StartCoroutine(SendHelloMessageContinuously(1f)); // Start publishing "hello" every 1 second
            }
            else
            {
                Debug.LogError("Failed to connect to MQTT broker.");
            }
        }
        catch (System.Exception ex)
        {
            Debug.LogError($"Error connecting to MQTT broker: {ex.Message}");
        }
    }

    void Update()
    {
        string message = "";
        if (Physics.Raycast(leftRay.Ray, out RaycastHit leftRayHit, leftRay.MaxRayLength)) // raycast to plane, if hit return hitpoint
        {
            message += "{leftHand: {x:" + (leftRayHit.point.x - colliderCenter.x) + ",y:" + (leftRayHit.point.y - colliderCenter.y) + "}"; // translate hitpoint to json
        }
        if (Physics.Raycast(rightRay.Ray, out RaycastHit rightRayHit, rightRay.MaxRayLength))
        {
            if (!message.Equals("")) // if left ray hit registered, edit json
            {
                message += ",";
            }
            message += "rightHand: {x:" + (rightRayHit.point.x - colliderCenter.x) + ",y:" + (rightRayHit.point.y - colliderCenter.y) + "}";
        }
        if (!message.Equals("")) // if either ray hit was registered
        {
            message += ",";
        }
        else
        {
            message += "{";

        }
        // return cube location
        message += "cube: {x:" + (cube.transform.position.x - colliderCenter.x) + ",y:" + (cube.transform.position.y - colliderCenter.y) + "}}";
        if (client != null && client.IsConnected) 
        {
            Debug.Log($"Publishing message: {message}");
            client.Publish(Topic, Encoding.UTF8.GetBytes(message)); // if client connected to broker, send the accumulated message
        }
        else
        {
            Debug.Log("MQTT client is not connected.");
        }
        /*if (publishAccum % 5 == 0)
        {
            
        }
        publishAccum++;*/
    }

    IEnumerator SendHelloMessageContinuously(float interval)
    {
        while (isPublishing)
        {
            if (client == null || !client.IsConnected)
            {
                Debug.LogWarning("MQTT client is not connected.");
                yield break;
            }

            // Publish the "hello" message
            string message = "hello";
            Debug.Log($"Publishing message: {message}");
            client.Publish(Topic, Encoding.UTF8.GetBytes(message));

            // Wait for the specified interval before sending the next message
            yield return new WaitForSeconds(interval);
        }
    }

    private void OnDestroy()
    {
        // Stop publishing and disconnect the MQTT client when the script is destroyed
        isPublishing = false;
        if (client != null && client.IsConnected)
        {
            client.Disconnect();
            Debug.Log("Disconnected from MQTT broker.");
        }
    }
}