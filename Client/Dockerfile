# ----- Build stage -----
FROM node:20-bullseye-slim AS build
WORKDIR /app

# העדפה לנעילה לפי package-lock.json
COPY package*.json ./

# הגדרות רשת/ריטרייז לנמוך תקלות הורדה
ENV NPM_CONFIG_REGISTRY="https://registry.npmjs.org/"
ENV NPM_CONFIG_PREFER_ONLINE=true
ENV NPM_CONFIG_FETCH_RETRIES=8
ENV NPM_CONFIG_FETCH_RETRY_FACTOR=2
ENV NPM_CONFIG_FETCH_RETRY_MINTIMEOUT=20000
ENV NPM_CONFIG_FETCH_RETRY_MAXTIMEOUT=120000

# ניקוי קאש והתקנה "אמינה" (נעילה + דאון-גרייד לניהול)
RUN rm -rf /root/.npm && npm cache clean --force \
  && npx -y npm@10.8.2 ci --no-audit --no-fund || \
     npx -y npm@10.8.2 install --no-audit --no-fund --legacy-peer-deps || \
     npx -y npm@10.8.2 install --no-audit --no-fund --force

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
FROM node:20-bullseye-slim AS runner
WORKDIR /app
RUN npm i -g serve
COPY --from=build /app/build ./build
ENV PORT=10000
EXPOSE 10000
CMD sh -c "serve -s build -l $PORT"
