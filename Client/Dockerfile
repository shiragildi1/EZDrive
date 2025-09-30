# ----- Build stage -----
FROM node:20-alpine AS build
WORKDIR /app

# קבצי package רק של הקליינט (כדי לקשֵש פחות את ה-cache)
COPY Client/package*.json ./
RUN npm ci

# שאר קוד הקליינט
COPY Client/ ./

# CRA קורא משתני סביבה שמתחילים ב-REACT_APP_ בזמן ה-build
# Render מעביר את ה-ENV גם בשלב ה-build, אבל נשים אותם במפורש
ARG REACT_APP_API_BASE
ARG REACT_APP_GOOGLE_CLIENT_ID
ENV REACT_APP_API_BASE=$REACT_APP_API_BASE
ENV REACT_APP_GOOGLE_CLIENT_ID=$REACT_APP_GOOGLE_CLIENT_ID

# build production
RUN npm run build

# ----- Run stage -----
FROM node:20-alpine AS runner
WORKDIR /app
RUN npm i -g serve

# נעתיק רק את תוצרי הבילד
COPY --from=build /app/build ./build

# Render מספק PORT; נשתמש בו
ENV PORT=10000
EXPOSE 10000

# להריץ את השרת הסטטי ולשמוע על $PORT
CMD sh -c "serve -s build -l $PORT"
