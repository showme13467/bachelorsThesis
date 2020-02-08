# importing the requests library
import requests

# api-endpoint
URL = "http://irt-beagle.cs.columbia.edu/api/devicesByPolygon"
API_awoke_URL = "",

DATA = {'geometry' : {'type' : 'Polygon','coordinates' : [[[-73.96098726600096,40.80964675109899],[-73.96104292562713,40.809674262775005],[-73.96101487100701,40.80972037013755],[-73.96095997415567,40.809693223402995],[-73.96098726600096,40.80964675109899]]]}}



# defining a params dict for the parameters to be sent to the API

# sending get request and saving the response as response object
r = requests.post(url=URL, json=DATA)

# extracting data in json format
data = r.json()

# extracting latitude, longitude and formatted address
# of the first matching location
#latitude = data['results'][0]['geometry']['location']['lat']
#longitude = data['results'][0]['geometry']['location']['lng']
#formatted_address = data['results'][0]['formatted_address']

# printing the output
#print("Latitude:%s\nLongitude:%s\nFormatted Address:%s"
 #     % (latitude, longitude, formatted_address))
print("Recieved Data:")
print(data)

try:
    data = data['data']

except:
    print(">>No Devices found!<<")

if(data):
        for x in data :
         print("Name of Device: " + x['name'])
         s = requests.get(url=x['url'])
         datanew = s.json()
         print("New recieved Data:" )
         print(datanew)
         try:
             datanew = datanew['data']
             if(datanew):
                for i in datanew:
                    print(i['name'])
         except:
             print(">>Can't read Data<<")
