# ----- Build stage -----
FROM node:20-alpine AS build
WORKDIR /app


COPY package*.json ./

RUN npm config set registry https://registry.npmjs.org/ \
    && npm config set cache /tmp/.npm --global \
    && npm ci --no-audit --no-fund


COPY . ./

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
