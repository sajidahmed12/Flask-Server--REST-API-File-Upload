
import flask
from flask import Flask, request, jsonify
from flask import render_template, redirect, url_for

from flask_sqlalchemy import SQLAlchemy 
from flask_marshmallow import Marshmallow 
import os

from werkzeug import secure_filename

# ---------------------------------------------------------------------------------

# Init app
app = Flask(__name__)
UPLOAD_FOLDER = 'uploads/'
app.config['UPLOAD_FOLDER'] = UPLOAD_FOLDER 
basedir = os.path.abspath(os.path.dirname(__file__))
# Database
app.config['SQLALCHEMY_DATABASE_URI'] = 'sqlite:///' + os.path.join(basedir, 'db.sqlite')
app.config['SQLALCHEMY_TRACK_MODIFICATIONS'] = False
# Init db
db = SQLAlchemy(app)
# Init ma
ma = Marshmallow(app)


# ---------------------------------------------------------------------------------


# FileObject Class/Model
class FileObject(db.Model):
  id = db.Column(db.Integer, primary_key=True)
  name = db.Column(db.String(100))
  file_path = db.Column(db.String(200))

  def __init__(self, name,file_path):
    self.name = name
    self.file_path = file_path


# FileObj Schema
class FileObjectSchema(ma.Schema):
  class Meta:
    fields = ('id', 'name', 'file_path')

# Init schema
FileObject_schema = FileObjectSchema()
FileObject_schema = FileObjectSchema(many=True)


# ---------------------------------------------------------------------------------

@app.route("/",methods=['GET'])
def home():
  if request.method =="GET":
    all_FileObjects = FileObject.query.all()
    result = FileObject_schema.dump(all_FileObjects)
    return render_template('show.html',content=result)

@app.route("/upload/", methods=['GET', 'POST', 'PUT'])
def upload():

  if request.method == "GET":
    return {"username": "admin","id": 42 }

  if request.method == "POST":

    #print(flask.request.files['image'])
    # print(request.json)
    # print(request.form)
    print(request.files['image'])
    # print(request.form['name'])
    # name = request.form['name']
    # file = request.files['file']
    # print(file)
    # temp_path = ""

    # if file:
    #   print('got a file! oky..')
    #   filename = secure_filename(file.filename)
    #   temp_path = app.config['UPLOAD_FOLDER']+filename
    #   file.save(temp_path)

    #   name = filename
    #   new_FileObject = FileObject(name, "../"+temp_path)

    #   db.session.add(new_FileObject)
    #   db.session.commit()
    #   return FileObject_schema.jsonify({"success"})
    # else:
    #   return FileObject_schema.jsonify({"failed"})

    #return FileObject_schema.jsonify({'request done!'})
    return {'request':'done!'}


@app.route('/delete/', methods=['GET','POST'])
def delete_file():

  if request.method == "POST":
    file_id = request.form['file_id']
    dump_file = FileObject.query.get(file_id)
    db.session.delete(dump_file)
    db.session.commit()

  return FileObject_schema.jsonify({'deleted'})

# ---------------------------------------------------------------------------------

# Run Server
if __name__ == '__main__':
  app.run(debug=True,host='0.0.0.0',port=8080)









