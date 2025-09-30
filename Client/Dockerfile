# ----- Build stage -----
FROM node:20-alpine AS build
WORKDIR /app

# קובצי חבילות
COPY package*.json ./

# הגדרות npm למנוע שליפות מקאש ולנסות שוב הורדות בעייתיות
RUN npm config set registry https://registry.npmjs.org/ \
  && npm config set cache /tmp/.npm --global \
  && npm config set prefer-online true \
  && npm config set fetch-retries 5 \
  && npm config set fetch-retry-factor 2 \
  && npm config set fetch-retry-mintimeout 20000 \
  && npm config set fetch-retry-maxtimeout 120000 \
  && npm cache clean --force \
  && npm ci --no-audit --no-fund

# קוד האפליקציה
COPY . ./

# משתני סביבה בזמן build (CRA קורא REACT_APP_ בזמן build)
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
COPY --from=build /app/build ./build

ENV PORT=10000
EXPOSE 10000
CMD sh -c "serve -s build -l $PORT"
