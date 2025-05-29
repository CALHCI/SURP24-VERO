using UnityEngine;
using UnityEngine.EventSystems;
using UnityEngine.UI;
using System.Net;
using System.Text;
using uPLibrary.Networking.M2Mqtt;

public class DrawOnTextureMQTT : MonoBehaviour, IPointerDownHandler, IDragHandler
{
    public RawImage rawImage;
    private Texture2D texture;
    private Color drawColor = Color.black;
    private int dotSize = 10; // Size of the dot

    // MQTT variables
    private MqttClient client;
    private const string Topic = "jgs/unity/test";
    private const string BrokerAddress = "test.mosquitto.org";
    private const int Port = 1883;

    void Start()
    {
        // Create a new texture with a white background
        texture = new Texture2D(512, 512);
        ClearTexture();
        rawImage.texture = texture;

        // Initialize MQTT client
        InitializeMqttClient();
    }

    void ClearTexture()
    {
        Color[] pixels = new Color[texture.width * texture.height];
        for (int i = 0; i < pixels.Length; i++)
        {
            pixels[i] = Color.white; // Set all pixels to white for the background
        }
        texture.SetPixels(pixels);
        texture.Apply();
    }

    private void InitializeMqttClient()
    {
        // Get the broker's IP address
        IPAddress brokerIpAddress = Dns.GetHostAddresses(BrokerAddress)[0];

        // Create and connect the MQTT client
        client = new MqttClient(brokerIpAddress, Port, false, null, null, MqttSslProtocols.None);
        string clientId = "UnityDrawClient_" + Random.Range(0, 1000);

        try
        {
            client.Connect(clientId);
            if (client.IsConnected)
            {
                Debug.Log("Connected to MQTT broker successfully.");
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

    public void OnPointerDown(PointerEventData eventData)
    {
        Draw(eventData);
    }

    public void OnDrag(PointerEventData eventData)
    {
        Draw(eventData);
    }

    void Draw(PointerEventData eventData)
    {
        RectTransform rectTransform = rawImage.GetComponent<RectTransform>();
        Vector2 localPoint;

        // Convert pointer position to local point
        RectTransformUtility.ScreenPointToLocalPointInRectangle(rectTransform, eventData.position, eventData.pressEventCamera, out localPoint);

        // Convert local point to texture coordinates
        float normalizedX = (localPoint.x + (rectTransform.rect.width * 0.5f)) / rectTransform.rect.width;
        float normalizedY = (localPoint.y + (rectTransform.rect.height * 0.5f)) / rectTransform.rect.height;

        int centerX = (int)(normalizedX * texture.width);
        int centerY = (int)(normalizedY * texture.height);

        // Draw a larger dot
        for (int x = -dotSize; x <= dotSize; x++)
        {
            for (int y = -dotSize; y <= dotSize; y++)
            {
                // Check if the pixel is within the texture bounds
                if (centerX + x >= 0 && centerX + x < texture.width && centerY + y >= 0 && centerY + y < texture.height)
                {
                    // Set the pixel color
                    texture.SetPixel(centerX + x, centerY + y, drawColor);
                }
            }
        }

        texture.Apply();

        float relativeX = (localPoint.x / rectTransform.rect.width) * 100f + 50f;
        float relativeY = (localPoint.y / rectTransform.rect.height) * 100f + 50f;

        // Send MQTT message with the coordinates
        SendMqttMessage(relativeX, relativeY);
    }

    private void SendMqttMessage(float x, float y)
    {
        if (client != null && client.IsConnected)
        {
            string message = $"{x:F2},{y:F2}"; // Create a message with coordinates
            Debug.Log($"Publishing message: {message}");
            client.Publish(Topic, Encoding.UTF8.GetBytes(message));
        }
        else
        {
            Debug.Log("MQTT client is not connected.");
        }
    }

    private void OnDestroy()
    {
        // Disconnect the MQTT client when the script is destroyed
        if (client != null && client.IsConnected)
        {
            client.Disconnect();
            Debug.Log("Disconnected from MQTT broker.");
        }
    }
}