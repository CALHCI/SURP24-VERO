using UnityEngine;
using System.Net;
using System.Text;
using System.Collections;
using uPLibrary.Networking.M2Mqtt;
using Oculus.Interaction;

public class MQTTPositionStreamer : MonoBehaviour
{

    private MqttClient client;
    private const string Topic = "jgs/unity/test";
    private bool isPublishing = true; // Flag to control publishing
    private const float interval = 1f;
    private int publishAccum = 0;

    public RayInteractor leftRay;
    public RayInteractor rightRay;
    public MeshCollider planeCollider;
    private Vector3 bottomCorner;

    void Start()
    {
        // Broker address and port
        string brokerAddress = "test.mosquitto.org";
        int port = 1883;

        // Get the broker's IP address
        IPAddress brokerIpAddress = Dns.GetHostAddresses(brokerAddress)[0];

        // Find bottom corner of plane
        bottomCorner = planeCollider.bounds.min;
        Debug.Log("Start Create Client");

        // Create and connect the MQTT client
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
        Ray RayL = leftRay.Ray;
        //Ray RayR = rightRay.Ray; // access rays of ray interactors
        if (publishAccum % 60 == 0)
        {
            if (Physics.Raycast(RayL,out RaycastHit leftRayHit,leftRay.MaxRayLength)) {
                if (client != null && client.IsConnected)
                {
                    // Publish the "hello" message
                    //string message = "{leftHand: {x:" + (leftRayHit.point.x - bottomCorner.x) + ",y:" + (leftRayHit.point.y - bottomCorner.y) + "}";
                    string message = "hello";
                    Debug.Log($"Publishing message: {message}");
                    client.Publish(Topic, Encoding.UTF8.GetBytes(message));
                }
                else
                {
                    Debug.Log("MQTT client is not connected.");
                }
            }
        }
        publishAccum++;
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