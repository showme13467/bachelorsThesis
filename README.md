# bachelorsThesis
Name Resolver for IoT Devices

[![Python Version](https://img.shields.io/badge/python-3.7.2-brightgreen.svg)](https://python.org)
[![Django Version](https://img.shields.io/badge/django-2.2.6-brightgreen.svg)](https://djangoproject.com)


![Homepage Example Screenshot](https://raw.githubusercontent.com/showme13467/bachelorsThesis/master/screenshotHomepage.PNG)


## Running the Project Locally

First, clone the repository to your local machine:

```bash
git clone https://github.com/showme13467/bachelorsThesis.git
```

Install the requirements:

```bash
pip install -r requirements.txt
```

Create MongoDB Cluster and name it *'mongoDB'* or update settings.py:

`DATABASES = {
    'default': {
        'ENGINE': 'djongo',
        'NAME': '<your DB name in MongoDB',
    }
}`


Finally, run the development server in NameResolver folder:

```bash
python manage.py runserver
```

The project will be available at **127.0.0.1:8000**.
