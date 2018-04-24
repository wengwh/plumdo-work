# Base image
FROM hub.c.163.com/library/java:8u111

copy ./simhei.ttf /usr/share/fonts

RUN cd /usr/share/fonts/ \
	&& chmod 755 * \
	&& fc-cache -f -v