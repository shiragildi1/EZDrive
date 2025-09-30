# ----- Build stage -----
FROM node:20-alpine AS build
WORKDIR /app


COPY package*.json ./
RUN npm ci


COPY . ./


ARG REACT_APP_API_BASE
ARG REACT_APP_GOOGLE_CLIENT_ID
ENV REACT_APP_API_BASE=$REACT_APP_API_BASE
ENV REACT_APP_GOOGLE_CLIENT_ID=$REACT_APP_GOOGLE_CLIENT_ID


RUN npm run build


FROM node:20-alpine AS runner
WORKDIR /app
RUN npm i -g serve


COPY --from=build /app/build ./build


ENV PORT=10000
EXPOSE 10000


CMD sh -c "serve -s build -l $PORT"
