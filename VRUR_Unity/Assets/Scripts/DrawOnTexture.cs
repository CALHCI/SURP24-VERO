using UnityEngine;
using UnityEngine.EventSystems;
using UnityEngine.UI;

public class DrawOnTexture : MonoBehaviour, IPointerDownHandler, IDragHandler
{
    public RawImage rawImage;
    private Texture2D texture;
    private Color drawColor = Color.black;
    private int dotSize = 10; // Size of the dot

    void Start()
    {
        // Create a new texture with a white background
        texture = new Texture2D(512, 512);
        ClearTexture();
        rawImage.texture = texture;
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

        // Convert pointer position to world point
        Vector3 worldPoint;
        RectTransformUtility.ScreenPointToWorldPointInRectangle(rectTransform, eventData.position, eventData.pressEventCamera, out worldPoint);

        // Convert world point to local point
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

        // Calculate relative position (0 to 100)
        float relativeX = (localPoint.x / rectTransform.rect.width) * 100f;
        float relativeY = (localPoint.y / rectTransform.rect.height) * 100f;

        // Print the relative position
        Debug.Log($"Relative Position: ({relativeX:F2}, {relativeY:F2})");
    }
}
