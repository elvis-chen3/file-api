This RESTful web service is designed for clients to upload files and retrieve content and metadata of a file.

The following instructions guides you through the setup of this application.
1. Uncompresss project zip file
2. Import project as a Maven project
3. Run as Spring Boot App

The following endpoints are provided for clients to interact with server.
HttpMethod.GET, "/files" It is used to retrieve metadata of all uploaded files.
HttpMethod.GET, "/file/{id}" It is used to retrieve metadata of a specific file of a given file id.
HttpMethod.POST, "/file" It is used to upload a file; then metadata of this file (e.g., file name, file size, date uploaded) 
will be stored in an in-memory database, and file itself is stored in the file system (upload-dir under current working space)
HttpMethod.GET, "/content/{id}" It is used to display file content from a specific file of a given file id. 
HttpMethod.GET, "/fileId?filename={filename}" It is used to retrieve id of a file that matches the given file name
HttpMethod.GET, "/poll" It is used to request for a poll for items which are uploaded last hour, and log the information.
