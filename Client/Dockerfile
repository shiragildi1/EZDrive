# ----- Build stage -----
FROM node:20-alpine AS build
WORKDIR /app

# רק קובצי החבילות
COPY package*.json ./

# הגדרות npm + התקנה עם fallback "תמיד תעבוד"
RUN npm config set registry https://registry.npmjs.org/ \
  && npm config set prefer-online true \
  && npm config set fetch-retries 5 \
  && npm config set fetch-retry-factor 2 \
  && npm config set fetch-retry-mintimeout 20000 \
  && npm config set fetch-retry-maxtimeout 120000 \
  && npm config set cache /tmp/.npm --global \
  && npm cache clean --force \
  && (npm install --no-audit --no-fund --legacy-peer-deps \
      || npm install --no-audit --no-fund --force)

# שאר הקוד
COPY . ./

# משתנים ש-CRA קורא בזמן build
ARG REACT_APP_API_BASE
ARG REACT_APP_GOOGLE_CLIENT_ID
ENV REACT_APP_API_BASE=$REACT_APP_API_BASE
ENV REACT_APP_GOOGLE_CLIENT_ID=$REACT_APP_GOOGLE_CLIENT_ID

# Build production
RUN npm run build

# ----- Run stage -----
FROM node:20-alpine AS runner
WORKDIR /app
RUN npm i -g serve
COPY --from=build /app/build ./build

ENV PORT=10000
EXPOSE 10000

CMD ["sh", "-c", "serve -s build -l $PORT"]
