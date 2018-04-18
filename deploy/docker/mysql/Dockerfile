# Base image
FROM hub.c.163.com/library/mysql:5.6

# Copy custom conf file(s)
COPY ./mysql.cnf /etc/mysql/conf.d/

# Make the conf files not writeable so mysql will read them
RUN chmod a-w /etc/mysql/conf.d/*