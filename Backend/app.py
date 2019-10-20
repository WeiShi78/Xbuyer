from flask import Flask, request
app = Flask(__name__)
# If you are using a Jupyter notebook, uncomment the following line.
# %matplotlib inline
import json
import requests
@app.route('/')
def hello_world():
    return 'Hello, World!'

@app.route('/image', methods = ['GET','POST'])
def imagepro():
    #file = request.files['file']
    if request.method == 'POST':
        file = request.files['file']
        print(file)
        subscription_key = "681bf9d5c5ad4e0d83625a32452ebf1c"
        ocr_url = "https://madhack.cognitiveservices.azure.com/" + "vision/v2.1/ocr"
        # Read the image into a byte array
        #file.save(secure_filename(file.filename))
       
        image_data = file

        # outfile1 = open('image.txt','w')
        # outfile1.write(image_data)
        # Set Content-Type to octet-stream
        headers = {'Ocp-Apim-Subscription-Key': subscription_key, 'Content-Type': 'application/octet-stream'}
        params = {'language': 'unk', 'detectOrientation': 'true'}
        # put the byte array into your post request
        response = requests.post(ocr_url, headers=headers, params=params, data = image_data)



        analysis = response.json()
        #print(analysis)

        response.raise_for_status()

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

        Item = []
        Date = []

        with open('./database.json') as json_file:
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
                    break

        return output

        