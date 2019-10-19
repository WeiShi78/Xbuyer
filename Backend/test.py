import requests
# If you are using a Jupyter notebook, uncomment the following line.
# %matplotlib inline
import matplotlib.pyplot as plt
from matplotlib.patches import Rectangle
import json
from PIL import Image
from io import BytesIO
import os;
import sys;
import json


# Add your Computer Vision subscription key and endpoint to your environment variables.
# if 'COMPUTER_VISION_SUBSCRIPTION_KEY' in os.environ:
#     subscription_key = os.environ['COMPUTER_VISION_SUBSCRIPTION_KEY']
# else:
#     print("\nSet the COMPUTER_VISION_SUBSCRIPTION_KEY environment variable.\n**Restart your shell or IDE for changes to take effect.**")
#     sys.exit()

# if 'COMPUTER_VISION_ENDPOINT' in os.environ:
#     endpoint = os.environ['COMPUTER_VISION_ENDPOINT']

subscription_key = "9f4c7e5aca7541868c870e9775a078d2"
ocr_url = "https://westcentralus.api.cognitive.microsoft.com/" + "vision/v2.1/ocr"

image_path = "/Users/weishi/Desktop/madhack/Xbuyer/Training_samples/Receipt_1.jpg"
# Read the image into a byte array
image_data = open(image_path, "rb").read()

# Set Content-Type to octet-stream
headers = {'Ocp-Apim-Subscription-Key': subscription_key, 'Content-Type': 'application/octet-stream'}
params = {'language': 'unk', 'detectOrientation': 'true'}
# put the byte array into your post request
response = requests.post(ocr_url, headers=headers, params=params, data = image_data)
response.raise_for_status()

analysis = response.json()

# Extract the word bounding boxes and text.
line_infos = [region["lines"] for region in analysis["regions"]]
word_infos = []
textArray = []
i = 0
for line in line_infos:
    for word_metadata in line:
        textArray.append("")
        for word_info in word_metadata["words"]:
            word_infos.append(word_info)
            textArray[i] = textArray[i] + (word_info['text']) + " "
        i = i+1


# Display the image and overlay it with the extracted text.
plt.figure(figsize=(5, 5))
image = Image.open(BytesIO(image_data))
ax = plt.imshow(image, alpha=0.5)
for word in word_infos:
    bbox = [int(num) for num in word["boundingBox"].split(",")]
    text = word["text"]
    origin = (bbox[0], bbox[1])
    patch = Rectangle(origin, bbox[2], bbox[3],
                      fill=False, linewidth=2, color='y')
    ax.axes.add_patch(patch)
    plt.text(origin[0], origin[1], text, fontsize=20, weight="bold", va="top")
plt.axis("off")

Item = []
Date = []

with open('../Backend/database.json') as json_file:
    foodData = json.load(json_file)
    for x in foodData['food']:
        Item.append(x['name'])
        Date.append(x['date'])

output = {}
output['food'] = []
for a in textArray:
    b = a.lower()
    for i in range(0, len(Item)):
        if Item[i] in b:
            output['food'].append({
                'name':Item[i],
                'date':Date[i]
            })

output
with open('output.json','w') as outfile:
    json.dump(output, outfile)



